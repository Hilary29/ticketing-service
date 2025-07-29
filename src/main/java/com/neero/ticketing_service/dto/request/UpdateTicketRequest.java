package com.neero.ticketing_service.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTicketRequest {

    @Pattern(regexp = "^(ACTIVE|USED|EXPIRED|CANCELLED)$", message = "Invalid status")
    private String status;

    @Future(message = "Validity date must be in the future")
    private LocalDateTime validityDate;

    @Size(max = 500, message = "Additional info must not exceed 500 characters")
    private String additionalInfo;
}
