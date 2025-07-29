package com.neero.ticketing_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TransferTicketRequest {
    @NotBlank(message = "Target user ID is required")
    private String targetUserId;
    
    private String transferReason;
    private String notes;
}