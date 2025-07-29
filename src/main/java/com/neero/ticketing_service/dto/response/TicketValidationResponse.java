package com.neero.ticketing_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketValidationResponse {

    private String ticketNumber;
    private Boolean valid;
    private String message;
    private String ticketType;
    private LocalDateTime usedAt;
}