package com.neero.ticketing_service.service;

import com.neero.ticketing_service.dto.request.CreateTicketRequest;
import com.neero.ticketing_service.dto.request.PaymentRequest;
import com.neero.ticketing_service.dto.request.UpdateTicketRequest;
import com.neero.ticketing_service.dto.response.PaymentResponse;
import com.neero.ticketing_service.dto.response.TicketResponse;
import com.neero.ticketing_service.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Service principal unifié pour la gestion complète des tickets
 * Combine ticket, payment et QR code en une interface cohérente
 */
public interface TicketingService {

    // === TICKET OPERATIONS ===
    
    TicketResponse createTicket(CreateTicketRequest request);
    
    TicketResponse getTicket(String ticketNumber);
    
    TicketResponse updateTicket(Long id, UpdateTicketRequest request);
    
    TicketResponse validateTicket(String ticketNumber);
    
    TicketResponse cancelTicket(String ticketNumber);
    
    Page<TicketResponse> getUserTickets(String publicUserId, Pageable pageable);
    
    Page<TicketResponse> getTicketsByStatus(TicketStatus status, Pageable pageable);
    
    List<TicketResponse> getTicketsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // === PAYMENT OPERATIONS ===
    
    PaymentResponse processPayment(PaymentRequest request);
    
    PaymentResponse getPayment(String transactionId);
    
    PaymentResponse refundPayment(String transactionId);
    
    // === ANALYTICS & REPORTING ===
    
    Map<String, Object> getStatistics(LocalDateTime startDate, LocalDateTime endDate);
    
    // === MAINTENANCE ===
    
    void processExpiredTickets();
    
    void processTimeoutPayments();
}