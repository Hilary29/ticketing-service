package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.entity.Payment;
import com.neero.ticketing_service.entity.Ticket;
import com.neero.ticketing_service.enums.PaymentMethod;
import com.neero.ticketing_service.enums.PaymentStatus;
import com.neero.ticketing_service.exception.ResourceNotFoundException;
import com.neero.ticketing_service.repository.PaymentRepository;
import com.neero.ticketing_service.repository.TicketRepository;
import com.neero.ticketing_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketRepository ticketRepository;

    @Override
    public Payment processPayment(Long ticketId, BigDecimal amount, PaymentMethod method) {
        log.info("Processing payment for ticket: {} with amount: {} using method: {}", ticketId, amount, method);
        
        Ticket ticket = ticketRepository.findById(ticketId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketId));
        
        Payment payment = Payment.builder()
            .transactionId(UUID.randomUUID().toString())
            .method(method)
            .status(PaymentStatus.COMPLETED) // Simulation: toujours réussi
            .amount(amount)
            .processedAt(LocalDateTime.now())
            .ticket(ticket)
            .build();
        
        return paymentRepository.save(payment);
    }

    @Override
    public String processPayment(PaymentMethod method, BigDecimal amount, Map<String, Object> paymentDetails) {
        log.info("Processing checkout payment with method: {} and amount: {}", method, amount);
        
        // Simulate payment processing based on method
        String transactionId = UUID.randomUUID().toString();
        
        // Here you would integrate with actual payment providers
        switch (method) {
            case NEERO_WALLET:
                log.info("Processing Neero wallet payment");
                break;
            case CREDIT_CARD:
                log.info("Processing credit card payment");
                break;
            case DEBIT_CARD:
                log.info("Processing debit card payment");
                break;
            case MOBILE_MONEY:
                log.info("Processing mobile money payment");
                break;
            case BANK_TRANSFER:
                log.info("Processing bank transfer payment");
                break;
            default:
                throw new RuntimeException("Unsupported payment method: " + method);
        }
        
        // For simulation, we always return success
        return transactionId;
    }

    @Override
    public void processRefund(String transactionId) {
        log.info("Processing refund for transaction: {}", transactionId);
        
        Payment payment = paymentRepository.findByTransactionId(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + transactionId));
        
        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);
    }
}