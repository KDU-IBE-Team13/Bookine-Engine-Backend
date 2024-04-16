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

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@RestController
@RequestMapping("/api/v1/configuration/footer")
public class FooterConfigurationController {

    private final FooterConfigService footerConfigService;
    private final String footerConfigAzureFilePath;

    public FooterConfigurationController(
        FooterConfigService footerConfigService,
        @Value("${config.footer.azure.file.path}") String footerConfigAzureFilePath
    ) {
        this.footerConfigService = footerConfigService;
        this.footerConfigAzureFilePath = footerConfigAzureFilePath;
    }
    
    @Cacheable(value = "footerConfigCache", key = "#root.methodName")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FooterConfigDTO> getFooterConfig() {
        FooterConfigDTO config = footerConfigService.loadConfigFromAzureBlob(footerConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    @CacheEvict(value = "footerConfigCache", allEntries = true)
    @PutMapping
    public ResponseEntity<String> updateFooterConfig(@RequestBody FooterConfigDTO updatedConfig) {
        footerConfigService.writeConfigToAzureBlob(updatedConfig, footerConfigAzureFilePath);
        return ResponseEntity.ok("Footer config updated successfully");
    }

}
