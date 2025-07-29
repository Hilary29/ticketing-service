package com.neero.ticketing_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PromoCodeRequest {
    @NotBlank(message = "Promo code is required")
    private String code;
}