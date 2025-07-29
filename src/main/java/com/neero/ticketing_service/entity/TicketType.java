package com.neero.ticketing_service.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "ticket_types", indexes = {
        @Index(name = "idx_ticket_type_code", columnList = "code", unique = true),
        @Index(name = "idx_ticket_type_active", columnList = "active")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TicketType extends BaseEntity {

    @Column(name = "code", unique = true, nullable = false)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "base_price", precision = 19, scale = 2, nullable = false)
    private BigDecimal basePrice;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Type(JsonType.class)
    @Column(name = "configuration", columnDefinition = "jsonb", nullable = false)
    @Builder.Default
    private Map<String, Object> configuration = new HashMap<>();
}