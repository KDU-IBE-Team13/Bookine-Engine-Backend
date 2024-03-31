package com.example.ibeproject;

import com.example.ibeproject.controller.RoomDetailsController;
import com.example.ibeproject.dto.roomdetails.RoomDetailsPriceListResponseDTO;
import com.example.ibeproject.dto.roomdetails.RoomDetailsPricesDTO;
import com.example.ibeproject.service.RoomAvailabilityDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RoomDetailsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RoomAvailabilityDetailsService roomAvailabilityDetailsService;

    @InjectMocks
    private RoomDetailsController roomAvailabilityDetailsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roomAvailabilityDetailsController).build();
    }

    @Test
    void testGetAvailableRoomDetails() throws Exception {
        // Sample response
        RoomDetailsPriceListResponseDTO responseDTO = new RoomDetailsPriceListResponseDTO();
        RoomDetailsPricesDTO room1 = new RoomDetailsPricesDTO(74, "SUPER_DELUXE", 410.0, 0, 2, 4, "Kickdrum", 100.0);
        RoomDetailsPricesDTO room2 = new RoomDetailsPricesDTO(77, "GARDEN_SUITE", 250.0, 2, 0, 2, "Kickdrum", 60.0);
        responseDTO.setRoomsDetails(Arrays.asList(room1, room2));

        when(roomAvailabilityDetailsService.getAvailableRoomDetails(
                anyInt(), anyInt(), anyString(), anyString(),
                anyInt(), anyInt(), anyDouble(), anyDouble(),
                anyInt(), anyList(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(responseDTO);

        // Perform GET request
        mockMvc.perform(get("/api/v1/rooms/available-room-details")
                .param("propertyId", "13")
                .param("tenantId", "1")
                .param("page", "1")
                .param("pageSize", "4")
                .param("checkInDate", "2024-03-01T00:00:00.000Z")
                .param("checkOutDate", "2024-03-11T00:00:00.000Z")
                .param("minPrice", "60")
                .param("maxPrice", "100")
                .param("beds", "2")
                .param("guestCount", "4")
                .param("bedType", "double")
                .param("sort", "price")
                .param("order", "dsc")
                .param("roomTypes", "DELUXE", "SUITE"))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.roomsDetails[0].roomTypeId").value(74))
                .andExpect(jsonPath("$.roomsDetails[0].roomTypeName").value("SUPER_DELUXE"))
                .andExpect(jsonPath("$.roomsDetails[0].areaInSquareFeet").value(410.0))
                .andExpect(jsonPath("$.roomsDetails[0].singleBed").value(0))
                .andExpect(jsonPath("$.roomsDetails[0].doubleBed").value(2))
                .andExpect(jsonPath("$.roomsDetails[0].maxCapacity").value(4))
                .andExpect(jsonPath("$.roomsDetails[0].propertyAddress").value("Kickdrum"))
                .andExpect(jsonPath("$.roomsDetails[0].basicNightlyPrice").value(100.0))
                .andExpect(jsonPath("$.roomsDetails[1].roomTypeId").value(77))
                .andExpect(jsonPath("$.roomsDetails[1].roomTypeName").value("GARDEN_SUITE"))
                .andExpect(jsonPath("$.roomsDetails[1].areaInSquareFeet").value(250.0))
                .andExpect(jsonPath("$.roomsDetails[1].singleBed").value(2))
                .andExpect(jsonPath("$.roomsDetails[1].doubleBed").value(0))
                .andExpect(jsonPath("$.roomsDetails[1].maxCapacity").value(2))
                .andExpect(jsonPath("$.roomsDetails[1].propertyAddress").value("Kickdrum"))
                .andExpect(jsonPath("$.roomsDetails[1].basicNightlyPrice").value(60.0));
    }
}
