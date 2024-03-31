package com.example.ibeproject.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.dto.roomdetails.RoomDetailsDTO;
import com.example.ibeproject.dto.roomdetails.RoomDetailsListResponseDTO;
import com.example.ibeproject.dto.roomdetails.RoomDetailsPriceListResponseDTO;
import com.example.ibeproject.service.RoomAvailabilityDetailsService;
import com.example.ibeproject.service.RoomAvailabilityService;
import com.example.ibeproject.service.RoomDetailsService;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomDetailsController {
    private final RoomDetailsService roomDetailsService;
    private final RoomAvailabilityService roomAvailabilityService;
    private final RoomAvailabilityDetailsService roomAvailabilityDetailsService;

    /**
     * Constructor for RoomDetailsController.
     * 
     * @param roomDetailsService      The service for managing room details
     * @param roomAvailabilityService service for managing room availability
     * @param roomAvailabilityService service for mapping room availability and
     *                                prices and applying filters
     */
    @Autowired
    public RoomDetailsController(RoomDetailsService roomDetailsService,
            RoomAvailabilityService roomAvailabilityService,
            RoomAvailabilityDetailsService roomAvailabilityDetailsService) {
        this.roomDetailsService = roomDetailsService;
        this.roomAvailabilityService = roomAvailabilityService;
        this.roomAvailabilityDetailsService = roomAvailabilityDetailsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoomDetailsDTO>> getAllRooms() {
        List<RoomDetailsDTO> roomDetails = roomDetailsService.getAllRooms();
        return ResponseEntity.ok(roomDetails);
    }

    @GetMapping(value = "/show-prices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomDetailsListResponseDTO> showPrices(
            @RequestParam int propertyId,
            @RequestParam int tenantId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {
        Map<String, Double> roomsPrices = roomDetailsService.getRoomDetails(tenantId, propertyId, checkInDate,
                checkOutDate);
        RoomDetailsListResponseDTO responseDTO = new RoomDetailsListResponseDTO(roomsPrices);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping(value = "/show-availability", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Boolean>> showAvailability(
            @RequestParam int propertyId,
            @RequestParam int tenantId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate) {

        Map<String, Boolean> roomAvailability = roomAvailabilityService.getRoomsAvailability(tenantId, propertyId,
                checkInDate, checkOutDate);
        return ResponseEntity.ok(roomAvailability);
    }

    /**
     * Reterives information about rooms
     * 
     * @return ResponseEntity containing the response DTO with room details and
     *         prices and filters applied.
     */
    @GetMapping(value = "/available-room-details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoomDetailsPriceListResponseDTO> getAvailableRoomDetails(
            @RequestParam int tenantId,
            @RequestParam int propertyId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "2") int pageSize,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer beds,
            @RequestParam(required = false) List<String> roomTypes,
            @RequestParam(required = false) Integer guestCount,
            @RequestParam(required = false) String bedType,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String order) {
        RoomDetailsPriceListResponseDTO response = roomAvailabilityDetailsService.getAvailableRoomDetails(tenantId,
                propertyId, checkInDate, checkOutDate, page, pageSize, minPrice, maxPrice, beds, roomTypes, guestCount,
                bedType, sort, order);
        return ResponseEntity.ok(response);

    }
}
