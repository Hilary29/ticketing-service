package com.neero.ticketing_service.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTicketStats {
    private String userId;
    private Long totalTickets;
    private Long activeTickets;
    private Long usedTickets;
    private Long expiredTickets;
    private BigDecimal totalAmountSpent;
    private BigDecimal averageTicketPrice;
    private String mostUsedTicketType;
}