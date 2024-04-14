package com.example.ibeproject.controller;

import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;
import com.example.ibeproject.service.ListBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/v1/listBookings")
public class ListBookingsController {

    private final ListBookingService listBookingsService;

    @Autowired
    public ListBookingsController(ListBookingService listBookingsService) {
        this.listBookingsService = listBookingsService;
    }

    @GetMapping(value = "/{billingEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BillingDetailsDTO>> getBillingDetailsById(@PathVariable String billingEmail) {
        try {
            List<BillingDetailsDTO> billingDetailsDTO = listBookingsService.getDetailsByEmail(billingEmail);
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
