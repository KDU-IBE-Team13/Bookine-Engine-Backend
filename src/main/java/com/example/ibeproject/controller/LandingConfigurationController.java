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

@RestController
@RequestMapping("/api/v1/configuration/landing-page")
public class LandingConfigurationController {

    private final LandingConfigService landingConfigService;
    private final String landingConfigAzureFilePath;

    /**
     * @param landingConfigService The service for managing landing page configurations.
     * @param landingConfigAzureFilePath The file path for storing landing page configuration data in Azure Blob Storage.
     */
    public LandingConfigurationController(
        LandingConfigService landingConfigService,
        @Value("${config.landing.azure.file.path}") String landingConfigAzureFilePath
    ) {
        this.landingConfigService = landingConfigService;
        this.landingConfigAzureFilePath = landingConfigAzureFilePath;
    }

    /**
     * Retrieves the current landing page configuration.
     * @return ResponseEntity containing the landing page configuration data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LandingConfigDTO> getLandingPageConfig() {
        LandingConfigDTO config = landingConfigService.loadConfigFromAzureBlob(landingConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    /**
     * Updates the landing page configuration.
     * @param updatedConfig The updated landing page configuration.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping
    public ResponseEntity<String> updateLandingPageConfig(@RequestBody LandingConfigDTO updatedConfig) {
        landingConfigService.writeConfigToAzureBlob(updatedConfig, landingConfigAzureFilePath);
        return ResponseEntity.ok("Landing page config updated successfully");
    }

}
