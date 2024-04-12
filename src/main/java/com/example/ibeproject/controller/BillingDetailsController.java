package com.example.ibeproject.controller;

import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;
import com.example.ibeproject.dto.rating.RatingDTO;
import com.example.ibeproject.service.BillingDetailsService;
import com.example.ibeproject.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/billingInfo")
public class BillingDetailsController {

    private final BillingDetailsService billingDetailsService;

    @Autowired
    public BillingDetailsController(BillingDetailsService billingDetailsService) {
        this.billingDetailsService = billingDetailsService;
    }

    /**
     * Creates a new billing detail.
     *
     * @param billingDetailsDTO The DTO containing billing details to be created.
     * @return ResponseEntity containing the created billing details or an error response.
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillingDetailsDTO> createRating(@RequestBody BillingDetailsDTO billingDetailsDTO) {
        try {
            BillingDetailsDTO billDetails = billingDetailsService.addDetails(billingDetailsDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(billDetails);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Retrieves billing details by ID.
     *
     * @param billingId The unique identifier of the billing details to retrieve.
     * @return ResponseEntity containing the billing details or an error response.
     */
    @GetMapping(value = "/{billingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BillingDetailsDTO> getBillingDetailsById(@PathVariable UUID billingId) {
        try {
            BillingDetailsDTO billingDetailsDTO = billingDetailsService.getDetailsById(billingId);
            if (billingDetailsDTO != null) {
                return ResponseEntity.ok(billingDetailsDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
