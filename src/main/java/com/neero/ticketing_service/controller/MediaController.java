package com.neero.ticketing_service.controller;

import com.neero.ticketing_service.dto.response.ApiResponse;
import com.neero.ticketing_service.dto.response.MediaDto;
import com.neero.ticketing_service.dto.response.MediaResource;
import com.neero.ticketing_service.enums.MediaContext;
import com.neero.ticketing_service.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping("/ticket-types/{typeCode}/images")
    public ApiResponse<MediaDto> uploadTicketTypeImage(
            @PathVariable String typeCode,
            @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(mediaService.uploadImage(typeCode, file, MediaContext.TICKET_TYPE));
    }

    @PostMapping("/ticket-types/{typeCode}/images/bulk")
    public ApiResponse<List<MediaDto>> uploadMultipleImages(
            @PathVariable String typeCode,
            @RequestParam("files") MultipartFile[] files) {
        return ApiResponse.success(mediaService.uploadMultiple(typeCode, files, MediaContext.TICKET_TYPE));
    }

    @GetMapping("/images/{mediaId}")
    public ResponseEntity<Resource> getImage(
            @PathVariable String mediaId,
            @RequestParam(defaultValue = "original") String size) {
        MediaResource resource = mediaService.getMedia(mediaId, size);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                       "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource.getResource());
    }

    @DeleteMapping("/{mediaId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deleteMedia(@PathVariable String mediaId) {
        mediaService.deleteMedia(mediaId);
        return ApiResponse.success("Media deleted successfully");
    }

    @GetMapping("/ticket-types/{typeCode}")
    public ApiResponse<List<MediaDto>> getTicketTypeMedia(@PathVariable String typeCode) {
        return ApiResponse.success(mediaService.getMediaByContext(typeCode, MediaContext.TICKET_TYPE));
    }

    @PutMapping("/{mediaId}/display-order")
    public ApiResponse<MediaDto> updateDisplayOrder(
            @PathVariable String mediaId,
            @RequestParam Integer displayOrder) {
        return ApiResponse.success(mediaService.updateDisplayOrder(mediaId, displayOrder));
    }
}