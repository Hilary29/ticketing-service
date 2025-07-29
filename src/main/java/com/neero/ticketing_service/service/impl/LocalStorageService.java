package com.neero.ticketing_service.service.impl;

import com.neero.ticketing_service.config.MediaStorageConfig;
import com.neero.ticketing_service.exception.ResourceNotFoundException;
import com.neero.ticketing_service.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "media.storage.type", havingValue = "local")
public class LocalStorageService implements StorageService {

    private final MediaStorageConfig config;

    @Override
    public String store(MultipartFile file, String directory) {
        try {
            String filename = generateUniqueFilename(file.getOriginalFilename());
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path directoryPath = Paths.get(config.getBasePath(), directory, datePath);
            
            Files.createDirectories(directoryPath);
            
            Path targetLocation = directoryPath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return directory + "/" + datePath + "/" + filename;
        } catch (IOException e) {
            log.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path filePath = Paths.get(config.getBasePath()).resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("File not found: " + filename);
        }
    }

    @Override
    public void delete(String filename) {
        try {
            Path filePath = Paths.get(config.getBasePath()).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: " + filename, e);
        }
    }

    @Override
    public String generateUrl(String filename) {
        return config.getBaseUrl() + "/" + filename;
    }

    private String generateUniqueFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}