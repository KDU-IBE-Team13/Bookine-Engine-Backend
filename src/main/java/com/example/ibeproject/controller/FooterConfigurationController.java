package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.footer.FooterConfigDTO;
import com.example.ibeproject.service.FooterConfigService;

@RestController
@RequestMapping("/api/v1/configuration/footer")
public class FooterConfigurationController {

    private final FooterConfigService footerConfigService;
    private final String footerConfigAzureFilePath;

    /**.
     * @param footerConfigService The service for managing footer configuration
     * @param footerConfigAzureFilePath The file path for storing footer configuration data in Azure Blob Storage.
     */
    public FooterConfigurationController(
        FooterConfigService footerConfigService,
        @Value("${config.footer.azure.file.path}") String footerConfigAzureFilePath
    ) {
        this.footerConfigService = footerConfigService;
        this.footerConfigAzureFilePath = footerConfigAzureFilePath;
    }
    
    /**
     * Retrieves the current footer configuration from the blob file
     * @return ResponseEntity containing the footer configuration data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FooterConfigDTO> getFooterConfig() {
        FooterConfigDTO config = footerConfigService.loadConfigFromAzureBlob(footerConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    /**
     * Updates the footer configuration in the blob file
     * @param updatedConfig The updated footer configuration.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping
    public ResponseEntity<String> updateFooterConfig(@RequestBody FooterConfigDTO updatedConfig) {
        footerConfigService.writeConfigToAzureBlob(updatedConfig, footerConfigAzureFilePath);
        return ResponseEntity.ok("Footer config updated successfully");
    }

}
