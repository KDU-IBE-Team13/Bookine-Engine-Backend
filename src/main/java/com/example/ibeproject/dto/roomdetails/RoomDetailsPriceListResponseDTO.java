package com.example.ibeproject.dto.roomdetails;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsPriceListResponseDTO {
    private List<RoomDetailsPricesDTO> roomsDetails;
    private int responseLength;
}
