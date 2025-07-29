package com.neero.ticketing_service.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile file, String directory);
    Resource load(String filename);
    void delete(String filename);
    String generateUrl(String filename);
}