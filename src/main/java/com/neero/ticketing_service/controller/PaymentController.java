package com.neero.ticketing_service.controller;

import com.neero.ticketing_service.dto.response.ApiResponse;
import com.neero.ticketing_service.dto.response.PaymentDto;
import com.neero.ticketing_service.entity.Payment;
import com.neero.ticketing_service.repository.PaymentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Operations for managing payments")
public class PaymentController {

    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<ApiResponse<List<PaymentDto>>> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentDto> paymentDtos = payments.stream()
                .map(payment -> {
                    PaymentDto dto = modelMapper.map(payment, PaymentDto.class);
                    if (payment.getTicket() != null) {
                        dto.setTicketNumber(payment.getTicket().getTicketNumber());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", paymentDtos));
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get payment by transaction ID")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByTransactionId(@PathVariable String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));
        
        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        if (payment.getTicket() != null) {
            paymentDto.setTicketNumber(payment.getTicket().getTicketNumber());
        }
        
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", paymentDto));
    }

    @GetMapping("/ticket/{ticketNumber}")
    @Operation(summary = "Get payment by ticket number")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentByTicketNumber(@PathVariable String ticketNumber) {
        Payment payment = paymentRepository.findByTicketTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Payment not found for ticket: " + ticketNumber));
        
        PaymentDto paymentDto = modelMapper.map(payment, PaymentDto.class);
        paymentDto.setTicketNumber(payment.getTicket().getTicketNumber());
        
        return ResponseEntity.ok(ApiResponse.success("Payment retrieved successfully", paymentDto));
    }
}