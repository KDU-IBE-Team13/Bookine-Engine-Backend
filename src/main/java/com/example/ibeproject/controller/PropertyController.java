package com.example.ibeproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.property.PropertyDTO;
import com.example.ibeproject.dto.property.PropertyListResponseDTO;
import com.example.ibeproject.service.PropertyService;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private final PropertyService propertyService;

    /**
     * Constructor for PropertyController.
     * @param propertyService The service for managing property data.
     */
    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    /**
     * Retrieves all properties.
     * @return ResponseEntity containing the response DTO with property data.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropertyListResponseDTO> getAllProperties() {
        List<PropertyDTO> properties = propertyService.getAllProperties();
        PropertyListResponseDTO responseDTO = new PropertyListResponseDTO(properties);
        
        return ResponseEntity.ok(responseDTO);
    }

}
