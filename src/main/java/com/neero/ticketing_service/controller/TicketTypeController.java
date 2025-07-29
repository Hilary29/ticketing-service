package com.neero.ticketing_service.controller;

import com.neero.ticketing_service.dto.request.CreateTicketTypeRequest;
import com.neero.ticketing_service.dto.response.ApiResponse;
import com.neero.ticketing_service.entity.TicketType;
import com.neero.ticketing_service.service.TicketTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ticket-types")
@RequiredArgsConstructor
@Tag(name = "Ticket Type Management", description = "Operations for managing ticket types")
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get all active ticket types")
    public ResponseEntity<ApiResponse<List<TicketType>>> getAllActiveTicketTypes() {
        List<TicketType> ticketTypes = ticketTypeService.getActiveTicketTypes();
        return ResponseEntity.ok(ApiResponse.success("Active ticket types retrieved successfully", ticketTypes));
    }

    @GetMapping("/{code}")
    @Operation(summary = "Get ticket type by code")
    public ResponseEntity<ApiResponse<TicketType>> getTicketTypeByCode(@PathVariable String code) {
        TicketType ticketType = ticketTypeService.getTicketTypeByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Ticket type retrieved successfully", ticketType));
    }

    @PostMapping
    @Operation(summary = "Create a new ticket type")
    public ResponseEntity<ApiResponse<TicketType>> createTicketType(
            @Valid @RequestBody CreateTicketTypeRequest request) {
        
        TicketType ticketType = modelMapper.map(request, TicketType.class);
        if (ticketType.getConfiguration() == null) {
            ticketType.setConfiguration(new HashMap<>());
        }
        
        TicketType createdTicketType = ticketTypeService.createTicketType(ticketType);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ticket type created successfully", createdTicketType));
    }
}