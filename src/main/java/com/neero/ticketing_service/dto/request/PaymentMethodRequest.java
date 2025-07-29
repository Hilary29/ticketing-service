package com.neero.ticketing_service.dto.request;

import com.neero.ticketing_service.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class PaymentMethodRequest {
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    private Map<String, Object> paymentDetails;
}