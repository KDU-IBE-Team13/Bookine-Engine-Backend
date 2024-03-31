package com.example.ibeproject.dto.roomdetails;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPricesDTO {
 private int roomTypeId;
 private String roomTypeName;
 private double basicNightlyRate;
 private String date;
}
