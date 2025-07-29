package com.neero.ticketing_service.service;

import com.neero.ticketing_service.dto.request.InitializeCheckoutRequest;
import com.neero.ticketing_service.dto.request.PaymentMethodRequest;
import com.neero.ticketing_service.dto.response.CheckoutResultDto;
import com.neero.ticketing_service.dto.response.CheckoutSessionDto;
import com.neero.ticketing_service.dto.response.CheckoutStatusDto;

import java.util.Map;

public interface CheckoutService {
    CheckoutSessionDto initialize(InitializeCheckoutRequest request);
    CheckoutSessionDto setPaymentMethod(String sessionId, PaymentMethodRequest request);
    CheckoutSessionDto applyPromoCode(String sessionId, String promoCode);
    CheckoutResultDto confirmAndPay(String sessionId, Map<String, Object> paymentDetails);
    CheckoutStatusDto getStatus(String sessionId);
    void cleanExpiredSessions();
}