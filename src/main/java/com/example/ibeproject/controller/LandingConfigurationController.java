package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.landing.LandingConfigDTO;
import com.example.ibeproject.service.LandingConfigService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/api/v1/configuration/landing-page")
public class LandingConfigurationController {

    private final LandingConfigService landingConfigService;
    private final String landingConfigAzureFilePath;

    public LandingConfigurationController(
        LandingConfigService landingConfigService,
        @Value("${config.landing.azure.file.path}") String landingConfigAzureFilePath
    ) {
        this.landingConfigService = landingConfigService;
        this.landingConfigAzureFilePath = landingConfigAzureFilePath;
    }

    @Cacheable(value = "landingPageConfigCache", key = "#root.methodName")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandingConfigDTO> getLandingPageConfig() {
        LandingConfigDTO config = landingConfigService.loadConfigFromAzureBlob(landingConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    @CacheEvict(value = "landingPageConfigCache", allEntries = true)
    @PutMapping
    public ResponseEntity<String> updateLandingPageConfig(@RequestBody LandingConfigDTO updatedConfig) {
        landingConfigService.writeConfigToAzureBlob(updatedConfig, landingConfigAzureFilePath);
        return ResponseEntity.ok("Landing page config updated successfully");
    }
}
