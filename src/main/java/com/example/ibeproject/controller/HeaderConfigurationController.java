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

@RestController
@RequestMapping("/api/v1/configuration/header")
public class HeaderConfigurationController {

    private final HeaderConfigService headerConfigService;
    private final String headerConfigAzureFilePath;

    /**
     * @param headerConfigService The service for managing header configurations.
     * @param headerConfigAzureFilePath The file path for storing header configuration data in Azure Blob Storage.
     */
    public HeaderConfigurationController(
        HeaderConfigService headerConfigService,
        @Value("${config.header.azure.file.path}") String headerConfigAzureFilePath
    ) {
        this.headerConfigService = headerConfigService;
        this.headerConfigAzureFilePath = headerConfigAzureFilePath;
    }

    /**
     * Retrieves the current header configuration.
     * @return ResponseEntity containing the header configuration data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeaderConfigDTO> getHeaderConfig() {
        HeaderConfigDTO config = headerConfigService.loadConfigFromAzureBlob(headerConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    /**
     * Updates the header configuration.
     * @param updatedConfig The updated header configuration.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping
    public ResponseEntity<String> updateHeaderConfig(@RequestBody HeaderConfigDTO updatedConfig) {
        headerConfigService.writeConfigToAzureBlob(updatedConfig, headerConfigAzureFilePath);
        return ResponseEntity.ok("Header config updated successfully");
    }

}
