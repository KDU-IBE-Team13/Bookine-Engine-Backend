package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.booking.BookingRequestDTO;
import com.example.ibeproject.exceptions.InsufficientRoomsException;
import com.example.ibeproject.service.BookingService;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Makes a new booking.
     *
     * @param bookingRequest The DTO containing booking request details.
     * @return ResponseEntity indicating success or failure of the booking operation.
     */
    @PostMapping
    public ResponseEntity<String> makeBooking(@RequestBody BookingRequestDTO bookingRequest) {
        try {
            boolean success = bookingService.makeBooking(bookingRequest);
            if (success) {
                return ResponseEntity.ok("Booking successful");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Booking failed");
            }
        } catch (InsufficientRoomsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
