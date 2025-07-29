package com.neero.ticketing_service.service;

import com.neero.ticketing_service.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TicketTypeService {

    TicketType createTicketType(TicketType ticketType);

    TicketType updateTicketType(Long id, TicketType ticketType);

    TicketType getTicketTypeById(Long id);

    TicketType getTicketTypeByCode(String code);

    Page<TicketType> getAllTicketTypes(Pageable pageable);

    Page<TicketType> getTicketTypesByCategory(String category, Pageable pageable);

    List<TicketType> getActiveTicketTypes();

    List<String> getAllCategories();

    void deactivateTicketType(Long id);

    void activateTicketType(Long id);
}
