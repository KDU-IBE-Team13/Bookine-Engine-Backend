package com.example.ibeproject.dto.roomdetails;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDetailsListResponseDTO {
    private Map<String, Double> rooms;
}
