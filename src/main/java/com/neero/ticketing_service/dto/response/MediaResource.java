package com.neero.ticketing_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@AllArgsConstructor
public class MediaResource {
    private Resource resource;
    private String contentType;
    private String filename;
    private Long size;
}