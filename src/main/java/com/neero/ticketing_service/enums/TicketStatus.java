package com.neero.ticketing_service.enums;

public enum TicketStatus {
    PENDING("Pending", "Ticket is pending payment"),
    PAID("Paid", "Ticket has been paid"),
    ACTIVE("Active", "Ticket is active and can be used"),
    USED("Used", "Ticket has been used"),
    EXPIRED("Expired", "Ticket has expired"),
    CANCELLED("Cancelled", "Ticket has been cancelled"),
    REFUNDED("Refunded", "Ticket has been refunded");
    
    private final String code;
    private final String description;
    
    TicketStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}
