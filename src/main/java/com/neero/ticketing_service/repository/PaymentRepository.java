package com.neero.ticketing_service.repository;

import com.neero.ticketing_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);
    
    Optional<Payment> findByTicketTicketNumber(String ticketNumber);
}
