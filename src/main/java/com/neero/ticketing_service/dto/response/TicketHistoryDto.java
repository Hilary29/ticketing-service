package com.neero.ticketing_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TicketHistoryDto {
    private Long id;
    private String ticketNumber;
    private String action;
    private String performedBy;
    private String previousStatus;
    private String newStatus;
    private String description;
    private Map<String, Object> metadata;
    private LocalDateTime timestamp;
}