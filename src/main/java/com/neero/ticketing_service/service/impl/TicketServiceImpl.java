package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.dto.request.CreateTicketRequest;
import com.neero.ticketing_service.dto.request.InitializeCheckoutRequest;
import com.neero.ticketing_service.dto.request.TicketSearchCriteria;
import com.neero.ticketing_service.dto.request.TransferTicketRequest;
import com.neero.ticketing_service.dto.response.TicketDto;
import com.neero.ticketing_service.dto.response.TicketHistoryDto;
import com.neero.ticketing_service.dto.response.UserTicketStats;
import com.neero.ticketing_service.entity.*;
import com.neero.ticketing_service.enums.PaymentMethod;
import com.neero.ticketing_service.enums.PaymentStatus;
import com.neero.ticketing_service.enums.TicketStatus;
import com.neero.ticketing_service.exception.ResourceNotFoundException;
import com.neero.ticketing_service.exception.ValidationException;
import com.neero.ticketing_service.repository.TicketRepository;
import com.neero.ticketing_service.repository.TicketTypeRepository;
import com.neero.ticketing_service.service.PaymentService;
import com.neero.ticketing_service.service.QRCodeService;
import com.neero.ticketing_service.service.TicketService;
import com.neero.ticketing_service.engine.GenericEngine;
import com.neero.ticketing_service.util.TicketNumberGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketNumberGenerator ticketNumberGenerator;
    private final GenericEngine engine;
    private final PaymentService paymentService;
    private final QRCodeService qrCodeService;

    @Override
    public TicketDto createTicket(CreateTicketRequest request) {
        log.info("Creating ticket for user: {} with type: {}", request.getUserId(), request.getTypeCode());
        
        // 1. Charger le type
        TicketType type = ticketTypeRepository.findByCodeAndActiveTrue(request.getTypeCode())
            .orElseThrow(() -> new ResourceNotFoundException("TicketType not found: " + request.getTypeCode()));
        
        // 2. Calculer le prix
        BigDecimal amount = engine.calculatePrice(
            type.getBasePrice(),
            request.getData(),
            type.getConfiguration()
        );
        
        // 3. Créer le ticket
        Ticket ticket = Ticket.builder()
            .ticketNumber(ticketNumberGenerator.generate())
            .userId(request.getUserId())
            .typeCode(type.getCode())
            .validUntil(engine.calculateValidity(type.getConfiguration()))
            .amount(amount)
            .data(request.getData())
            .build();
        
        // 4. Valider
        engine.validate(ticket, type.getConfiguration());
        
        // 5. Sauvegarder
        ticket = ticketRepository.save(ticket);
        
        // 6. Paiement
        Payment payment = paymentService.processPayment(
            ticket.getId(),
            amount,
            PaymentMethod.valueOf(request.getPaymentMethod())
        );
        
        // 7. Activer si payé
        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            ticket.setStatus(TicketStatus.ACTIVE);
            ticket.setQrCode(qrCodeService.generateQRCode(ticket.getTicketNumber()));
            ticketRepository.save(ticket);
        }
        
        return toDto(ticket, type.getName());
    }

    @Override
    public TicketDto getTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        TicketType type = ticketTypeRepository.findByCode(ticket.getTypeCode())
            .orElseThrow(() -> new ResourceNotFoundException("TicketType not found: " + ticket.getTypeCode()));
        
        return toDto(ticket, type.getName());
    }

    @Override
    public TicketDto validateTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new IllegalStateException("Ticket is not active: " + ticket.getStatus());
        }
        
        ticket.setStatus(TicketStatus.USED);
        Ticket updatedTicket = ticketRepository.save(ticket);
        
        TicketType type = ticketTypeRepository.findByCode(updatedTicket.getTypeCode())
            .orElseThrow(() -> new ResourceNotFoundException("TicketType not found: " + updatedTicket.getTypeCode()));
        
        return toDto(ticket, type.getName());
    }

    @Override
    public void cancelTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Cannot cancel used ticket");
        }
        
        ticket.setStatus(TicketStatus.CANCELLED);
        ticketRepository.save(ticket);
        
        // Traiter le remboursement si nécessaire
        if (ticket.getPayment() != null) {
            paymentService.processRefund(ticket.getPayment().getTransactionId());
        }
    }
    
    @Override
    public Page<TicketDto> searchTickets(TicketSearchCriteria criteria, Pageable pageable) {
        // Simple implementation - would need proper JPA Criteria API in production
        List<Ticket> allTickets = ticketRepository.findAll();
        List<Ticket> filteredTickets = allTickets.stream()
                .filter(ticket -> criteria.getUserId() == null || ticket.getUserId().equals(criteria.getUserId()))
                .filter(ticket -> criteria.getStatus() == null || ticket.getStatus().name().equals(criteria.getStatus()))
                .filter(ticket -> criteria.getTypeCode() == null || ticket.getTypeCode().equals(criteria.getTypeCode()))
                .collect(Collectors.toList());

        List<TicketDto> ticketDtos = filteredTickets.stream()
                .map(ticket -> {
                    TicketType type = ticketTypeRepository.findByCode(ticket.getTypeCode()).orElse(null);
                    return toDto(ticket, type != null ? type.getName() : "Unknown");
                })
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), ticketDtos.size());
        List<TicketDto> pageContent = ticketDtos.subList(start, end);

        return new PageImpl<>(pageContent, pageable, ticketDtos.size());
    }

    @Override
    public UserTicketStats getUserStats(String userId) {
        List<Ticket> userTickets = ticketRepository.findByUserId(userId);
        
        UserTicketStats stats = new UserTicketStats();
        stats.setUserId(userId);
        stats.setTotalTickets((long) userTickets.size());
        stats.setActiveTickets(userTickets.stream().mapToLong(t -> t.getStatus() == TicketStatus.ACTIVE ? 1 : 0).sum());
        stats.setUsedTickets(userTickets.stream().mapToLong(t -> t.getStatus() == TicketStatus.USED ? 1 : 0).sum());
        stats.setExpiredTickets(userTickets.stream().mapToLong(t -> t.getStatus() == TicketStatus.EXPIRED ? 1 : 0).sum());
        stats.setTotalAmountSpent(userTickets.stream().map(Ticket::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
        
        if (!userTickets.isEmpty()) {
            stats.setAverageTicketPrice(stats.getTotalAmountSpent().divide(BigDecimal.valueOf(userTickets.size())));
            stats.setMostUsedTicketType(userTickets.stream()
                    .collect(Collectors.groupingBy(Ticket::getTypeCode, Collectors.counting()))
                    .entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .orElse("None"));
        } else {
            stats.setAverageTicketPrice(BigDecimal.ZERO);
            stats.setMostUsedTicketType("None");
        }
        
        return stats;
    }

    @Override
    public TicketDto transferTicket(String ticketNumber, TransferTicketRequest request) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        if (ticket.getStatus() != TicketStatus.ACTIVE) {
            throw new ValidationException("Only active tickets can be transferred");
        }
        
        ticket.setUserId(request.getTargetUserId());
        Ticket savedTicket = ticketRepository.save(ticket);
        
        TicketType type = ticketTypeRepository.findByCode(savedTicket.getTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException("TicketType not found"));
        
        return toDto(savedTicket, type.getName());
    }

    @Override
    public byte[] generateQRCodeImage(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        return qrCodeService.generateQRCodeImage(ticket.getQrCode());
    }

    @Override
    public byte[] generateTicketFile(String ticketNumber, String format) {
        // Simple implementation - would need proper PDF/document generation
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        String content = String.format("TICKET: %s\nSTATUS: %s\nAMOUNT: %s", 
                ticket.getTicketNumber(), ticket.getStatus(), ticket.getAmount());
        
        return content.getBytes();
    }

    @Override
    public TicketDto renewTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + ticketNumber));
        
        if (ticket.getStatus() != TicketStatus.EXPIRED) {
            throw new ValidationException("Only expired tickets can be renewed");
        }
        
        ticket.setStatus(TicketStatus.ACTIVE);
        ticket.setValidUntil(LocalDateTime.now().plusDays(30)); // Extend by 30 days
        Ticket savedTicket = ticketRepository.save(ticket);
        
        TicketType type = ticketTypeRepository.findByCode(savedTicket.getTypeCode())
                .orElseThrow(() -> new ResourceNotFoundException("TicketType not found"));
        
        return toDto(savedTicket, type.getName());
    }

    @Override
    public List<TicketHistoryDto> getTicketHistory(String ticketNumber) {
        // Simple implementation - would need proper audit table in production
        TicketHistoryDto history = new TicketHistoryDto();
        history.setTicketNumber(ticketNumber);
        history.setAction("CREATED");
        history.setTimestamp(LocalDateTime.now());
        history.setDescription("Ticket created");
        
        return List.of(history);
    }

    @Override
    public List<String> createTicketsFromRequest(List<InitializeCheckoutRequest.TicketItem> ticketItems, String userId, String transactionId) {
        List<String> ticketNumbers = new ArrayList<>();
        
        for (InitializeCheckoutRequest.TicketItem item : ticketItems) {
            TicketType ticketType = ticketTypeRepository.findByCode(item.getTypeCode())
                    .orElseThrow(() -> new ValidationException("Invalid ticket type: " + item.getTypeCode()));
            
            for (int i = 0; i < item.getQuantity(); i++) {
                CreateTicketRequest request = new CreateTicketRequest();
                request.setUserId(userId);
                request.setTypeCode(item.getTypeCode());
                request.setAmount(ticketType.getBasePrice());
                request.setPaymentMethod(PaymentMethod.NEERO_WALLET);
                
                Map<String, Object> data = new HashMap<>();
                data.put("transactionId", transactionId);
                request.setData(data);
                
                TicketDto ticketDto = createTicket(request);
                ticketNumbers.add(ticketDto.getTicketNumber());
            }
        }
        
        return ticketNumbers;
    }

    private TicketDto toDto(Ticket ticket, String typeName) {
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setTicketNumber(ticket.getTicketNumber());
        dto.setTypeCode(ticket.getTypeCode());
        dto.setTypeName(typeName);
        dto.setAmount(ticket.getAmount());
        dto.setStatus(ticket.getStatus().name());
        dto.setValidUntil(ticket.getValidUntil());
        dto.setQrCode(ticket.getQrCode());
        dto.setData(ticket.getData());
        return dto;
    }
}