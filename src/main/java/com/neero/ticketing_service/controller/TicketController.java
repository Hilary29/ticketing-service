package com.neero.ticketing_service.controller;

import com.neero.ticketing_service.dto.request.CreateTicketRequest;
import com.neero.ticketing_service.dto.request.TicketSearchCriteria;
import com.neero.ticketing_service.dto.request.TransferTicketRequest;
import com.neero.ticketing_service.dto.response.ApiResponse;
import com.neero.ticketing_service.dto.response.TicketDto;
import com.neero.ticketing_service.dto.response.TicketHistoryDto;
import com.neero.ticketing_service.dto.response.UserTicketStats;
import com.neero.ticketing_service.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Ticket Management", description = "Simplified ticket operations")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @Operation(summary = "Create a new ticket")
    public ResponseEntity<ApiResponse<TicketDto>> createTicket(
            @Valid @RequestBody CreateTicketRequest request) {

        TicketDto ticket = ticketService.createTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ticket created successfully", ticket));
    }

    @GetMapping("/{ticketNumber}")
    @Operation(summary = "Get ticket by number")
    public ResponseEntity<ApiResponse<TicketDto>> getTicket(
            @PathVariable String ticketNumber) {
        TicketDto ticket = ticketService.getTicket(ticketNumber);
        return ResponseEntity.ok(ApiResponse.success(ticket));
    }

    @PostMapping("/validate/{ticketNumber}")
    @Operation(summary = "Validate a ticket")
    public ResponseEntity<ApiResponse<TicketDto>> validateTicket(
            @PathVariable String ticketNumber) {
        TicketDto ticket = ticketService.validateTicket(ticketNumber);
        return ResponseEntity.ok(ApiResponse.success("Ticket validated successfully", ticket));
    }

    @DeleteMapping("/{ticketNumber}")
    @Operation(summary = "Cancel a ticket")
    public ResponseEntity<ApiResponse<Void>> cancelTicket(
            @PathVariable String ticketNumber) {
        ticketService.cancelTicket(ticketNumber);
        return ResponseEntity.ok(ApiResponse.success("Ticket cancelled successfully", null));
    }

    // 🔍 Recherche et Filtrage
    @GetMapping
    public ApiResponse<Page<TicketDto>> searchTickets(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String typeCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Pageable pageable) {
        TicketSearchCriteria criteria = new TicketSearchCriteria();
        criteria.setUserId(userId);
        criteria.setStatus(status);
        criteria.setTypeCode(typeCode);
        criteria.setFromDate(fromDate);
        criteria.setToDate(toDate);
        return ApiResponse.success(ticketService.searchTickets(criteria, pageable));
    }

    // 📊 Statistiques utilisateur
    @GetMapping("/user/{userId}/stats")
    public ApiResponse<UserTicketStats> getUserTicketStats(@PathVariable String userId) {
        return ApiResponse.success(ticketService.getUserStats(userId));
    }

    // 🔄 Transférer un ticket
    @PostMapping("/{ticketNumber}/transfer")
    public ApiResponse<TicketDto> transferTicket(
            @PathVariable String ticketNumber,
            @RequestBody @Valid TransferTicketRequest request) {
        return ApiResponse.success(ticketService.transferTicket(ticketNumber, request));
    }

    // 📱 Générer QR Code
    @GetMapping("/{ticketNumber}/qr-code")
    @ResponseBody
    public ResponseEntity<byte[]> getQRCode(@PathVariable String ticketNumber) {
        byte[] qrCode = ticketService.generateQRCodeImage(ticketNumber);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(qrCode);
    }

    // 📄 Télécharger PDF du ticket
    @GetMapping("/{ticketNumber}/download")
    public ResponseEntity<byte[]> downloadTicket(
            @PathVariable String ticketNumber,
            @RequestParam(defaultValue = "pdf") String format) {
        byte[] ticketFile = ticketService.generateTicketFile(ticketNumber, format);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "attachment; filename=ticket-" + ticketNumber + "." + format)
                .body(ticketFile);
    }

    // 🔄 Renouveler un ticket expiré
    @PostMapping("/{ticketNumber}/renew")
    public ApiResponse<TicketDto> renewTicket(@PathVariable String ticketNumber) {
        return ApiResponse.success(ticketService.renewTicket(ticketNumber));
    }

    // 📝 Historique des modifications
    @GetMapping("/{ticketNumber}/history")
    public ApiResponse<List<TicketHistoryDto>> getTicketHistory(@PathVariable String ticketNumber) {
        return ApiResponse.success(ticketService.getTicketHistory(ticketNumber));
    }
}