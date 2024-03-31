package com.example.ibeproject.dto.roomdetails;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDetailsPricesDTO {
    private int roomTypeId;
    private String roomTypeName;
    private double areaInSquareFeet;
    private int singleBed;
    private int doubleBed;
    private int maxCapacity;
    private String propertyAddress;
    private double basicNightlyPrice;
}
