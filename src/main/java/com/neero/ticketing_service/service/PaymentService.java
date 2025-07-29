package com.neero.ticketing_service.service;

import com.neero.ticketing_service.entity.Payment;
import com.neero.ticketing_service.enums.PaymentMethod;

import java.math.BigDecimal;
import java.util.Map;

public interface PaymentService {

    Payment processPayment(Long ticketId, BigDecimal amount, PaymentMethod method);

    String processPayment(PaymentMethod method, BigDecimal amount, Map<String, Object> paymentDetails);

    void processRefund(String transactionId);
}

