package com.neero.ticketing_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CheckoutStatusDto {
    private String sessionId;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime expiresAt;
    private String message;
    private Integer progress; // 0-100
}