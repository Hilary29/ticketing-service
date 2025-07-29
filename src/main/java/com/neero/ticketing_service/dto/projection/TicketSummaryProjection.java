package com.neero.ticketing_service.dto.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Simple projection for ticket summary views
 */
public interface TicketSummaryProjection {
    Long getId();
    String getTicketNumber();
    String getName();
    LocalDateTime getPurchaseDate();
    LocalDateTime getValidityDate();
    BigDecimal getTotalAmount();
    String getStatus();
    String getPublicUserId();
}