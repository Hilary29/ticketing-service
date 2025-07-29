package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.entity.TicketType;
import com.neero.ticketing_service.repository.TicketTypeRepository;
import com.neero.ticketing_service.service.TicketTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;

    @Override
    public TicketType createTicketType(TicketType ticketType) {
        return ticketTypeRepository.save(ticketType);
    }

    @Override
    public TicketType updateTicketType(Long id, TicketType ticketType) {
        TicketType existing = getTicketTypeById(id);
        existing.setName(ticketType.getName());
        existing.setBasePrice(ticketType.getBasePrice());
        existing.setConfiguration(ticketType.getConfiguration());
        existing.setActive(ticketType.getActive());
        return ticketTypeRepository.save(existing);
    }

    @Override
    public TicketType getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TicketType not found with id: " + id));
    }

    @Override
    public TicketType getTicketTypeByCode(String code) {
        return ticketTypeRepository.findByCodeAndActiveTrue(code)
                .orElseThrow(() -> new RuntimeException("TicketType not found: " + code));
    }

    @Override
    public Page<TicketType> getAllTicketTypes(Pageable pageable) {
        return ticketTypeRepository.findAll(pageable);
    }

    @Override
    public Page<TicketType> getTicketTypesByCategory(String category, Pageable pageable) {
        // Implementation can be added later if categories are needed
        return ticketTypeRepository.findAll(pageable);
    }

    @Override
    public List<TicketType> getActiveTicketTypes() {
        return ticketTypeRepository.findByActive(true);
    }

    @Override
    public List<String> getAllCategories() {
        // Implementation can be added later if categories are needed
        return List.of();
    }

    @Override
    public void deactivateTicketType(Long id) {
        TicketType ticketType = getTicketTypeById(id);
        ticketType.setActive(false);
        ticketTypeRepository.save(ticketType);
    }

    @Override
    public void activateTicketType(Long id) {
        TicketType ticketType = getTicketTypeById(id);
        ticketType.setActive(true);
        ticketTypeRepository.save(ticketType);
    }
}
