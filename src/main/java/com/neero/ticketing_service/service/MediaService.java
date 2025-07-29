package com.neero.ticketing_service.service;

import com.neero.ticketing_service.dto.response.MediaDto;
import com.neero.ticketing_service.dto.response.MediaResource;
import com.neero.ticketing_service.enums.MediaContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {
    MediaDto uploadImage(String contextId, MultipartFile file, MediaContext context);
    List<MediaDto> uploadMultiple(String contextId, MultipartFile[] files, MediaContext context);
    MediaResource getMedia(String mediaId, String size);
    void deleteMedia(String mediaId);
    List<MediaDto> getMediaByContext(String contextId, MediaContext context);
    MediaDto updateDisplayOrder(String mediaId, Integer displayOrder);
    boolean isValidImageType(String contentType);
}