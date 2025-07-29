package com.neero.ticketing_service.service;

import com.neero.ticketing_service.dto.request.CreateTicketRequest;
import com.neero.ticketing_service.dto.request.InitializeCheckoutRequest;
import com.neero.ticketing_service.dto.request.TicketSearchCriteria;
import com.neero.ticketing_service.dto.request.TransferTicketRequest;
import com.neero.ticketing_service.dto.response.TicketDto;
import com.neero.ticketing_service.dto.response.TicketHistoryDto;
import com.neero.ticketing_service.dto.response.UserTicketStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketService {

    TicketDto createTicket(CreateTicketRequest request);

    TicketDto getTicket(String ticketNumber);

    TicketDto validateTicket(String ticketNumber);

    void cancelTicket(String ticketNumber);

    // New methods for enhanced functionality
    Page<TicketDto> searchTickets(TicketSearchCriteria criteria, Pageable pageable);
    
    UserTicketStats getUserStats(String userId);
    
    TicketDto transferTicket(String ticketNumber, TransferTicketRequest request);
    
    byte[] generateQRCodeImage(String ticketNumber);
    
    byte[] generateTicketFile(String ticketNumber, String format);
    
    TicketDto renewTicket(String ticketNumber);
    
    List<TicketHistoryDto> getTicketHistory(String ticketNumber);
    
    List<String> createTicketsFromRequest(List<InitializeCheckoutRequest.TicketItem> ticketItems, String userId, String transactionId);
}
