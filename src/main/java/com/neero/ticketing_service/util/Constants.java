package com.neero.ticketing_service.util;


public final class Constants {

    private Constants() {
        // Private constructor to prevent instantiation
    }

    // API Version
    public static final String API_VERSION = "/api/v1";

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 100;

    // Ticket
    public static final String TICKET_NUMBER_PATTERN = "^[A-Z]{3}-\\d{14}-\\d{4}-\\d{3}$";
    public static final int TICKET_QR_CODE_SIZE = 350;
    public static final int TICKET_DEFAULT_VALIDITY_DAYS = 30;

    // Payment
    public static final int PAYMENT_TIMEOUT_MINUTES = 15;
    public static final int PAYMENT_MAX_RETRY_ATTEMPTS = 3;

    // Security
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    // Error Messages
    public static final String RESOURCE_NOT_FOUND = "Resource not found";
    public static final String INVALID_REQUEST = "Invalid request";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access";
    public static final String PAYMENT_FAILED = "Payment processing failed";
    public static final String TICKET_EXPIRED = "Ticket has expired";
    public static final String TICKET_ALREADY_USED = "Ticket has already been used";

    // Success Messages
    public static final String OPERATION_SUCCESS = "Operation completed successfully";
    public static final String TICKET_CREATED = "Ticket created successfully";
    public static final String PAYMENT_SUCCESS = "Payment processed successfully";
    public static final String TICKET_VALIDATED = "Ticket validated successfully";
}

