package com.neero.ticketing_service.exception;

import lombok.Getter;

@Getter
public class PaymentFailedException extends RuntimeException {

    private final String errorCode;

    public PaymentFailedException(String message) {
        super(message);
        this.errorCode = "PAYMENT_FAILED";
    }

    public PaymentFailedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "PAYMENT_FAILED";
    }

}
