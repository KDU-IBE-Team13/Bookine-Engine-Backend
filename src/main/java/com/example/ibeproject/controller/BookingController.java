package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ibeproject.service.BookingService;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<Integer> createBooking(
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int adultCount,
            @RequestParam int childCount,
            @RequestParam double totalCost,
            @RequestParam double amountDueAtResort,
            @RequestParam String guestName,
            @RequestParam int promotionId,
            @RequestParam int propertyId) {
        int totalCostInt = (int) Math.round(totalCost);
        int amountDueAtResortInt = (int) Math.round(amountDueAtResort);

        Integer bookingId = bookingService.createBooking(checkInDate, checkOutDate, adultCount, childCount,
                totalCostInt, amountDueAtResortInt, guestName, promotionId, propertyId);
        if (bookingId == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        int[] roomAvailabilityIds = {175943, 175944 }; //coded for now

        Integer roomAvailabilitiesUpdated = bookingService.updateRoomAvailabilities(roomAvailabilityIds, bookingId);
        
        if(roomAvailabilitiesUpdated != roomAvailabilityIds.length){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        return ResponseEntity.ok(roomAvailabilitiesUpdated);
    }

}