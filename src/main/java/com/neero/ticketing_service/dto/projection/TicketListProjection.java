package com.neero.ticketing_service.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Minimal projection for ticket lists
 */
public interface TicketListProjection {
    Long getId();
    String getTicketNumber();
    String getName();
    BigDecimal getTotalAmount();
    String getStatus();
    LocalDateTime getPurchaseDate();
}