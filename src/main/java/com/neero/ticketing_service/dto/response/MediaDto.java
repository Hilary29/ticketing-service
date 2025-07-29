package com.neero.ticketing_service.dto.response;

import com.neero.ticketing_service.enums.MediaContext;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class MediaDto {
    private Long id;
    private String mediaId;
    private String originalFilename;
    private String contentType;
    private Long size;
    private MediaContext context;
    private String contextId;
    private String url;
    private String thumbnailUrl;
    private Map<String, String> variants;
    private Map<String, Object> metadata;
    private Integer displayOrder;
    private Boolean active;
    private LocalDateTime createdAt;
}