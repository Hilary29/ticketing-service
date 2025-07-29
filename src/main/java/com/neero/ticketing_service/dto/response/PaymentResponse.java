package com.neero.ticketing_service.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private String transactionId;
    private String ticketNumber;
    private String method;
    private String status;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String referenceNumber;
    private String failureReason;
    private Long ticketId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
