package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.dto.request.InitializeCheckoutRequest;
import com.neero.ticketing_service.dto.request.PaymentMethodRequest;
import com.neero.ticketing_service.dto.response.CheckoutResultDto;
import com.neero.ticketing_service.dto.response.CheckoutSessionDto;
import com.neero.ticketing_service.dto.response.CheckoutStatusDto;
import com.neero.ticketing_service.entity.CheckoutSession;
import com.neero.ticketing_service.entity.TicketType;
import com.neero.ticketing_service.exception.ResourceNotFoundException;
import com.neero.ticketing_service.exception.ValidationException;
import com.neero.ticketing_service.repository.CheckoutSessionRepository;
import com.neero.ticketing_service.repository.TicketTypeRepository;
import com.neero.ticketing_service.service.CheckoutService;
import com.neero.ticketing_service.service.PaymentService;
import com.neero.ticketing_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutSessionRepository checkoutRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentService paymentService;
    private final TicketService ticketService;

    @Override
    @Transactional
    public CheckoutSessionDto initialize(InitializeCheckoutRequest request) {
        if (request.getTicketItems() == null || request.getTicketItems().isEmpty()) {
            throw new ValidationException("Ticket items are required");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (InitializeCheckoutRequest.TicketItem item : request.getTicketItems()) {
            TicketType ticketType = ticketTypeRepository.findByCode(item.getTypeCode())
                    .orElseThrow(() -> new ValidationException("Invalid ticket type: " + item.getTypeCode()));
            
            BigDecimal itemTotal = ticketType.getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        CheckoutSession session = new CheckoutSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(request.getUserId());
        session.setCartId(null); // No cart needed
        session.setTotalAmount(totalAmount);
        session.setDiscountAmount(BigDecimal.ZERO);
        session.setPromoCode(request.getPromoCode());
        session.setStatus("INITIALIZED");
        Map<String, Object> metadata = request.getMetadata() != null ? new HashMap<>(request.getMetadata()) : new HashMap<>();
        metadata.put("originalRequest", request);
        session.setMetadata(metadata);
        session.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        CheckoutSession saved = checkoutRepository.save(session);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public CheckoutSessionDto setPaymentMethod(String sessionId, PaymentMethodRequest request) {
        CheckoutSession session = getActiveSession(sessionId);
        
        session.setPaymentMethod(request.getPaymentMethod());
        session.setPaymentDetails(request.getPaymentDetails());
        session.setStatus("PAYMENT_SET");

        CheckoutSession saved = checkoutRepository.save(session);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public CheckoutSessionDto applyPromoCode(String sessionId, String promoCode) {
        CheckoutSession session = getActiveSession(sessionId);
        
        // TODO: Implement promo code validation and discount calculation
        session.setPromoCode(promoCode);
        // session.setDiscountAmount(calculatedDiscount);
        // session.setTotalAmount(session.getTotalAmount().subtract(calculatedDiscount));

        CheckoutSession saved = checkoutRepository.save(session);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public CheckoutResultDto confirmAndPay(String sessionId, Map<String, Object> paymentDetails) {
        CheckoutSession session = getActiveSession(sessionId);
        
        if (!"PAYMENT_SET".equals(session.getStatus())) {
            throw new ValidationException("Payment method must be set before confirmation");
        }

        try {
            // Process payment
            String transactionId = paymentService.processPayment(
                    session.getPaymentMethod(),
                    session.getTotalAmount(),
                    paymentDetails
            );

            // Create tickets from checkout session
            InitializeCheckoutRequest originalRequest = (InitializeCheckoutRequest) session.getMetadata().get("originalRequest");
            if (originalRequest == null) {
                throw new ValidationException("Original request data not found in session");
            }
            
            List<String> ticketNumbers = ticketService.createTicketsFromRequest(originalRequest.getTicketItems(), session.getUserId(), transactionId);

            // Update session
            session.setStatus("COMPLETED");
            session.setCompletedAt(LocalDateTime.now());
            checkoutRepository.save(session);

            CheckoutResultDto result = new CheckoutResultDto();
            result.setSessionId(sessionId);
            result.setStatus("COMPLETED");
            result.setTotalAmount(session.getTotalAmount());
            result.setTransactionId(transactionId);
            result.setTicketNumbers(ticketNumbers);
            result.setCompletedAt(session.getCompletedAt());
            result.setMessage("Payment successful and tickets created");

            return result;

        } catch (Exception e) {
            session.setStatus("FAILED");
            checkoutRepository.save(session);
            
            CheckoutResultDto result = new CheckoutResultDto();
            result.setSessionId(sessionId);
            result.setStatus("FAILED");
            result.setMessage("Payment failed: " + e.getMessage());
            
            return result;
        }
    }

    @Override
    public CheckoutStatusDto getStatus(String sessionId) {
        CheckoutSession session = checkoutRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkout session not found"));

        CheckoutStatusDto status = new CheckoutStatusDto();
        status.setSessionId(sessionId);
        status.setStatus(session.getStatus());
        status.setTotalAmount(session.getTotalAmount());
        status.setExpiresAt(session.getExpiresAt());
        status.setProgress(calculateProgress(session.getStatus()));
        status.setMessage(getStatusMessage(session.getStatus()));

        return status;
    }

    @Override
    @Scheduled(fixedRate = 300000) // Every 5 minutes
    @Transactional
    public void cleanExpiredSessions() {
        List<CheckoutSession> expiredSessions = checkoutRepository.findExpiredSessions(LocalDateTime.now());
        expiredSessions.forEach(session -> session.setStatus("EXPIRED"));
        checkoutRepository.saveAll(expiredSessions);
        log.info("Cleaned {} expired checkout sessions", expiredSessions.size());
    }

    private CheckoutSession getActiveSession(String sessionId) {
        CheckoutSession session = checkoutRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkout session not found"));

        if ("EXPIRED".equals(session.getStatus()) || "COMPLETED".equals(session.getStatus())) {
            throw new ValidationException("Checkout session is no longer active");
        }

        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus("EXPIRED");
            checkoutRepository.save(session);
            throw new ValidationException("Checkout session has expired");
        }

        return session;
    }

    private CheckoutSessionDto convertToDto(CheckoutSession session) {
        CheckoutSessionDto dto = new CheckoutSessionDto();
        BeanUtils.copyProperties(session, dto);
        return dto;
    }

    private Integer calculateProgress(String status) {
        return switch (status) {
            case "INITIALIZED" -> 25;
            case "PAYMENT_SET" -> 50;
            case "PROCESSING" -> 75;
            case "COMPLETED" -> 100;
            case "FAILED", "EXPIRED" -> 0;
            default -> 0;
        };
    }

    private String getStatusMessage(String status) {
        return switch (status) {
            case "INITIALIZED" -> "Checkout session initialized";
            case "PAYMENT_SET" -> "Payment method set, ready to confirm";
            case "PROCESSING" -> "Processing payment...";
            case "COMPLETED" -> "Payment completed successfully";
            case "FAILED" -> "Payment failed";
            case "EXPIRED" -> "Checkout session expired";
            default -> "Unknown status";
        };
    }
}