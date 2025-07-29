package com.neero.ticketing_service.controller;

import com.neero.ticketing_service.dto.request.InitializeCheckoutRequest;
import com.neero.ticketing_service.dto.request.PaymentMethodRequest;
import com.neero.ticketing_service.dto.request.PromoCodeRequest;
import com.neero.ticketing_service.dto.response.ApiResponse;
import com.neero.ticketing_service.dto.response.CheckoutResultDto;
import com.neero.ticketing_service.dto.response.CheckoutSessionDto;
import com.neero.ticketing_service.dto.response.CheckoutStatusDto;
import com.neero.ticketing_service.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/initialize")
    public ApiResponse<CheckoutSessionDto> initializeCheckout(
            @RequestBody @Valid InitializeCheckoutRequest request) {
        return ApiResponse.success(checkoutService.initialize(request));
    }

    @PutMapping("/session/{sessionId}/payment-method")
    public ApiResponse<CheckoutSessionDto> setPaymentMethod(
            @PathVariable String sessionId,
            @RequestBody @Valid PaymentMethodRequest request) {
        return ApiResponse.success(checkoutService.setPaymentMethod(sessionId, request));
    }

    @PostMapping("/session/{sessionId}/promo-code")
    public ApiResponse<CheckoutSessionDto> applyPromoCode(
            @PathVariable String sessionId,
            @RequestBody @Valid PromoCodeRequest request) {
        return ApiResponse.success(checkoutService.applyPromoCode(sessionId, request.getCode()));
    }

    @PostMapping("/session/{sessionId}/confirm")
    public ApiResponse<CheckoutResultDto> confirmCheckout(
            @PathVariable String sessionId,
            @RequestBody(required = false) Map<String, Object> paymentDetails) {
        return ApiResponse.success(checkoutService.confirmAndPay(sessionId, paymentDetails));
    }

    @GetMapping("/session/{sessionId}/status")
    public ApiResponse<CheckoutStatusDto> getCheckoutStatus(@PathVariable String sessionId) {
        return ApiResponse.success(checkoutService.getStatus(sessionId));
    }
}