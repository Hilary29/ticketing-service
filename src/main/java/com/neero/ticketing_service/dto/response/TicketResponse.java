package com.neero.ticketing_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TicketResponse {

    // Informations essentielles uniquement
    private Long id;
    private String ticketNumber;
    private String name;
    private LocalDateTime purchaseDate;
    private LocalDateTime validityDate;
    private BigDecimal totalAmount;
    private String status;
    private String qrCode;
    private String publicUserId;
    
    // Items simplifiés en JSON natif
    private String itemsJson;
    
    // Payment info minimal
    private String paymentTransactionId;
    private String paymentStatus;

}
