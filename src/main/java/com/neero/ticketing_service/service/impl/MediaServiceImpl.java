package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.config.MediaStorageConfig;
import com.neero.ticketing_service.dto.response.MediaDto;
import com.neero.ticketing_service.dto.response.MediaResource;
import com.neero.ticketing_service.entity.Media;
import com.neero.ticketing_service.enums.MediaContext;
import com.neero.ticketing_service.exception.ResourceNotFoundException;
import com.neero.ticketing_service.exception.ValidationException;
import com.neero.ticketing_service.repository.MediaRepository;
import com.neero.ticketing_service.service.MediaService;
import com.neero.ticketing_service.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;
    private final StorageService storageService;
    private final MediaStorageConfig config;

    @Override
    @Transactional
    public MediaDto uploadImage(String contextId, MultipartFile file, MediaContext context) {
        validateFile(file);
        
        String mediaId = UUID.randomUUID().toString();
        String storedPath = storageService.store(file, context.name().toLowerCase());
        
        Media media = new Media();
        media.setMediaId(mediaId);
        media.setOriginalFilename(file.getOriginalFilename());
        media.setStoredFilename(storedPath);
        media.setContentType(file.getContentType());
        media.setSize(file.getSize());
        media.setContext(context);
        media.setContextId(contextId);
        media.setUrl(storageService.generateUrl(storedPath));
        media.setActive(true);
        media.setDisplayOrder(getNextDisplayOrder(contextId, context));
        
        Media saved = mediaRepository.save(media);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public List<MediaDto> uploadMultiple(String contextId, MultipartFile[] files, MediaContext context) {
        return Arrays.stream(files)
                .map(file -> uploadImage(contextId, file, context))
                .collect(Collectors.toList());
    }

    @Override
    public MediaResource getMedia(String mediaId, String size) {
        Media media = mediaRepository.findByMediaId(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: " + mediaId));

        String filename = getFilenameForSize(media, size);
        Resource resource = storageService.load(filename);
        
        return new MediaResource(resource, media.getContentType(), media.getOriginalFilename(), media.getSize());
    }

    @Override
    @Transactional
    public void deleteMedia(String mediaId) {
        Media media = mediaRepository.findByMediaId(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: " + mediaId));
        
        try {
            storageService.delete(media.getStoredFilename());
            if (media.getVariants() != null) {
                media.getVariants().values().forEach(storageService::delete);
            }
        } catch (Exception e) {
            log.warn("Failed to delete files for media: " + mediaId, e);
        }
        
        mediaRepository.deleteByMediaId(mediaId);
    }

    @Override
    public List<MediaDto> getMediaByContext(String contextId, MediaContext context) {
        return mediaRepository.findActiveMediaByContext(context, contextId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MediaDto updateDisplayOrder(String mediaId, Integer displayOrder) {
        Media media = mediaRepository.findByMediaId(mediaId)
                .orElseThrow(() -> new ResourceNotFoundException("Media not found: " + mediaId));
        
        media.setDisplayOrder(displayOrder);
        Media saved = mediaRepository.save(media);
        return convertToDto(saved);
    }

    @Override
    public boolean isValidImageType(String contentType) {
        if (config.getAllowedTypes() == null || config.getAllowedTypes().isEmpty()) {
            return contentType != null && contentType.startsWith("image/");
        }
        return config.getAllowedTypes().contains(contentType);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ValidationException("File is empty");
        }
        
        if (!isValidImageType(file.getContentType())) {
            throw new ValidationException("Invalid file type: " + file.getContentType());
        }
        
        if (config.getMaxFileSize() != null && file.getSize() > config.getMaxFileSize()) {
            throw new ValidationException("File size exceeds maximum allowed size");
        }
    }

    private Integer getNextDisplayOrder(String contextId, MediaContext context) {
        List<Media> existingMedia = mediaRepository.findByContextAndContextId(context, contextId);
        return existingMedia.stream()
                .mapToInt(media -> media.getDisplayOrder() != null ? media.getDisplayOrder() : 0)
                .max()
                .orElse(0) + 1;
    }

    private String getFilenameForSize(Media media, String size) {
        if ("original".equals(size) || media.getVariants() == null) {
            return media.getStoredFilename();
        }
        return media.getVariants().getOrDefault(size, media.getStoredFilename());
    }

    private MediaDto convertToDto(Media media) {
        MediaDto dto = new MediaDto();
        BeanUtils.copyProperties(media, dto);
        return dto;
    }
}