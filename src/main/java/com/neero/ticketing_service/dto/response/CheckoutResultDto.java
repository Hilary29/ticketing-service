package com.neero.ticketing_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CheckoutResultDto {
    private String sessionId;
    private String status;
    private BigDecimal totalAmount;
    private String transactionId;
    private List<String> ticketNumbers;
    private LocalDateTime completedAt;
    private String message;
}