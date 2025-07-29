package com.neero.ticketing_service.entity;

import com.neero.ticketing_service.enums.PaymentMethod;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "checkout_session")
public class CheckoutSession extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String sessionId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long cartId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private String promoCode;

    @Column(nullable = false)
    private String status; // INITIALIZED, PAYMENT_SET, CONFIRMED, COMPLETED, EXPIRED, CANCELLED

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> paymentDetails;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    private LocalDateTime expiresAt;

    private LocalDateTime completedAt;
}