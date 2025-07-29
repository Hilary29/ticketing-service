package com.neero.ticketing_service.repository;

import com.neero.ticketing_service.entity.CheckoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckoutSessionRepository extends JpaRepository<CheckoutSession, Long> {
    
    Optional<CheckoutSession> findBySessionId(String sessionId);
    
    List<CheckoutSession> findByUserIdOrderByCreatedAtDesc(String userId);
    
    @Query("SELECT cs FROM CheckoutSession cs WHERE cs.expiresAt < :now AND cs.status IN ('INITIALIZED', 'PAYMENT_SET')")
    List<CheckoutSession> findExpiredSessions(@Param("now") LocalDateTime now);
    
    @Query("SELECT cs FROM CheckoutSession cs WHERE cs.userId = :userId AND cs.status = :status")
    List<CheckoutSession> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);
}