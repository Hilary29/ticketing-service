package com.neero.ticketing_service.dto.response;

import com.neero.ticketing_service.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class CheckoutSessionDto {
    private String sessionId;
    private String userId;
    private PaymentMethod paymentMethod;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String promoCode;
    private String status;
    private Map<String, Object> paymentDetails;
    private Map<String, Object> metadata;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}