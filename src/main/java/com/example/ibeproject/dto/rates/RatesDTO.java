package com.example.ibeproject.dto.rates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatesDTO {
    private String roomTypeName;
    private int basicNightlyRate;
    private String date;

}