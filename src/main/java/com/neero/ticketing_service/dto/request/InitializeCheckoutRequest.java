package com.neero.ticketing_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InitializeCheckoutRequest {
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotEmpty(message = "Ticket items are required")
    private List<TicketItem> ticketItems;
    
    private String promoCode;
    private Map<String, Object> metadata;
    
    @Data
    public static class TicketItem {
        @NotBlank(message = "Type code is required")
        private String typeCode;
        
        @NotBlank(message = "Quantity is required")
        private Integer quantity;
    }
}