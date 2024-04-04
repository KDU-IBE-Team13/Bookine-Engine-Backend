package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.roomtype.RoomTypeConfigDTO;
import com.example.ibeproject.service.RoomTypeConfigService;

@RestController
@RequestMapping("/api/v1/configuration/room-type-details")
public class RoomTypeConfigurationController {

    private final RoomTypeConfigService roomTypeConfigService;
    private final String roomTypeConfigAzureFilePath;

    /**
     * @param roomTypeConfigService The service for managing roomType configurations.
     * @param roomTypeConfigAzureFilePath The file path for storing roomType configuration data in Azure Blob Storage.
     */
    public RoomTypeConfigurationController(
        RoomTypeConfigService roomTypeConfigService,
        @Value("${config.roomType.azure.file.path}") String roomTypeConfigAzureFilePath
    ) {
        this.roomTypeConfigService = roomTypeConfigService;
        this.roomTypeConfigAzureFilePath = roomTypeConfigAzureFilePath;
    }

    /**
     * Retrieves the current roomType configuration.
     * @return ResponseEntity containing the roomType configuration data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomTypeConfigDTO> getRoomTypeConfig() {
        RoomTypeConfigDTO config = roomTypeConfigService.loadConfigFromAzureBlob(roomTypeConfigAzureFilePath);
        return ResponseEntity.ok(config);
    }

    /**
     * Updates the roomType configuration.
     * @param updatedConfig The updated roomType configuration.
     * @return ResponseEntity indicating the success of the update operation.
     */
    @PutMapping
    public ResponseEntity<String> updateRoomTypeConfig(@RequestBody RoomTypeConfigDTO updatedConfig) {
        roomTypeConfigService.writeConfigToAzureBlob(updatedConfig, roomTypeConfigAzureFilePath);
        return ResponseEntity.ok("room type details config updated successfully");
    }

}
