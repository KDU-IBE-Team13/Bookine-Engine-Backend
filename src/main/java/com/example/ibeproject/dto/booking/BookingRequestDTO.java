package com.example.ibeproject.dto.booking;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDTO {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int roomTypeId;
    private int numRooms;

}
