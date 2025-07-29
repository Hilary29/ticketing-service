package com.neero.ticketing_service.entity;

import com.neero.ticketing_service.enums.MediaContext;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "media")
public class Media extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String mediaId;

    @Column(nullable = false)
    private String originalFilename;

    @Column(nullable = false)
    private String storedFilename;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    private MediaContext context;

    @Column(nullable = false)
    private String contextId;

    private String url;

    private String thumbnailUrl;

    @ElementCollection
    @CollectionTable(name = "media_variants", joinColumns = @JoinColumn(name = "media_id"))
    @MapKeyColumn(name = "variant_name")
    @Column(name = "variant_url")
    private Map<String, String> variants;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    private Integer displayOrder;

    @Column(nullable = false)
    private Boolean active = true;
}