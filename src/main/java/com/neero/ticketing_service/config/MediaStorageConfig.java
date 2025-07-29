package com.neero.ticketing_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "media.storage")
public class MediaStorageConfig {
    private String type;
    private String basePath;
    private String baseUrl;
    private Map<String, String> s3Config;
    private Map<String, String> cloudinaryConfig;
    private List<String> allowedTypes;
    private Long maxFileSize;
    private Map<String, SizeConfig> imageSizes;

    @Data
    public static class SizeConfig {
        private Integer width;
        private Integer height;
        private String quality;
    }
}