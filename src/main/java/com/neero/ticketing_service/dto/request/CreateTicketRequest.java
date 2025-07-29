package com.neero.ticketing_service.dto.request;

import com.neero.ticketing_service.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class CreateTicketRequest {

    @NotBlank(message = "Type code is required")
    private String typeCode;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotNull(message = "Data is required")
    private Map<String, Object> data;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;
    
    // Additional fields for cart functionality
    private BigDecimal amount;
    private PaymentMethod paymentMethodEnum;
    
    public void setPaymentMethod(PaymentMethod method) {
        this.paymentMethodEnum = method;
        this.paymentMethod = method.getCode();
    }
}