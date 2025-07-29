package com.neero.ticketing_service.repository;

import com.neero.ticketing_service.entity.TicketType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    Optional<TicketType> findByCode(String code);

    Optional<TicketType> findByCodeAndActiveTrue(String code);

    List<TicketType> findByActive(Boolean active);

    boolean existsByCode(String code);
}
