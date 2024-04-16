package com.example.ibeproject.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createBooking(
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam int adultCount,
            @RequestParam int childCount,
            @RequestParam double totalCost,
            @RequestParam double amountDueAtResort,
            @RequestParam String guestName,
            @RequestParam int promotionId,
            @RequestParam int propertyId,
            @RequestParam int roomTypeId,
            @RequestParam int rooms) {
        int totalCostInt = (int) Math.round(totalCost);
        int amountDueAtResortInt = (int) Math.round(amountDueAtResort);

        List<Integer> availabilityIds = bookingService.getAvailableRoomDetails(checkInDate, checkOutDate, roomTypeId, propertyId, rooms);
        System.out.println(availabilityIds);
        if(availabilityIds.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        Integer bookingId = bookingService.createBooking(checkInDate, checkOutDate, adultCount, childCount,
                totalCostInt, amountDueAtResortInt, guestName, promotionId, propertyId);

        if (bookingId == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }

        Integer roomAvailabilitiesUpdated = bookingService.updateRoomAvailabilities(availabilityIds, bookingId, rooms);

        // if (roomAvailabilitiesUpdated != rooms) {
        //     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        // }

        return ResponseEntity.ok(roomAvailabilitiesUpdated);
    }

}



