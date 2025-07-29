package com.neero.ticketing_service.dto.response;

import com.neero.ticketing_service.enums.PaymentMethod;
import com.neero.ticketing_service.enums.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Long id;
    private String transactionId;
    private PaymentMethod method;
    private PaymentStatus status;
    private BigDecimal amount;
    private LocalDateTime processedAt;
    private String ticketNumber;
    private LocalDateTime createdAt;
}