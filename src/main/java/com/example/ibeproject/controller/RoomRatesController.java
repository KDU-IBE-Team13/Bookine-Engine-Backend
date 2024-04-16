package com.example.ibeproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.ibeproject.dto.rates.RatesDTO;
import com.example.ibeproject.service.RoomRatesService;

@RestController
@RequestMapping("/api/v1/rates")
public class RoomRatesController {

    private final RoomRatesService roomRatesService;

    @Autowired
    public RoomRatesController(RoomRatesService roomRatesService) {
        this.roomRatesService = roomRatesService;
    }


   @GetMapping(value = "/price", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatesDTO> getRoomRateWithinDatesByRoomTypeId(@RequestParam int tenantId,@RequestParam int propertyId,@RequestParam int roomTypeId,@RequestParam String localCheckInDate,@RequestParam String localCheckOutDate) {
        return roomRatesService.getPricesInRange(tenantId, propertyId, roomTypeId, localCheckInDate, localCheckOutDate);
    }
    
}
