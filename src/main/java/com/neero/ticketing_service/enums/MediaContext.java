package com.neero.ticketing_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MediaContext {
    TICKET_TYPE("Ticket Type"),
    TICKET("Ticket"),
    PAYMENT("Payment"),
    USER_PROFILE("User Profile");

    private final String displayName;
}