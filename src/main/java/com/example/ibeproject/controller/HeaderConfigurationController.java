package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.example.ibeproject.service.HeaderConfigService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/api/v1/configuration/header")
public class HeaderConfigurationController {

    private final HeaderConfigService headerConfigService;
    private final String headerConfigAzureFilePath;

    public HeaderConfigurationController(
        HeaderConfigService headerConfigService,
        @Value("${config.header.azure.file.path}") String headerConfigAzureFilePath
    ) {
        this.headerConfigService = headerConfigService;
        this.headerConfigAzureFilePath = headerConfigAzureFilePath;
    }
    
    @Cacheable(value = "headerConfigCache", key = "#root.methodName")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeaderConfigDTO> getHeaderConfig() {
        HeaderConfigDTO config = headerConfigService.loadConfigFromAzureBlob(headerConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    @CacheEvict(value = "headerConfigCache", allEntries = true)
    @PutMapping
    public ResponseEntity<String> updateHeaderConfig(@RequestBody HeaderConfigDTO updatedConfig) {
        headerConfigService.writeConfigToAzureBlob(updatedConfig, headerConfigAzureFilePath);
        return ResponseEntity.ok("Header config updated successfully");
    }

}
