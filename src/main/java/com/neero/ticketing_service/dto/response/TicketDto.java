package com.neero.ticketing_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class TicketDto {
    private Long id;
    private String ticketNumber;
    private String typeCode;
    private String typeName;
    private BigDecimal amount;
    private String status;
    private LocalDateTime validUntil;
    private String qrCode;
    private Map<String, Object> data;
}