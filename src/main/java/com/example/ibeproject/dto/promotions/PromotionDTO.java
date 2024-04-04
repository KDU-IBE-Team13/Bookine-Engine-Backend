package com.example.ibeproject.dto.promotions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromotionDTO {
    private int promotionId;
    private String promotionTitle;
    private String promotionDescription;
    private double priceFactor;
    private int minimumDaysOfStay;
    private boolean isDeactivated;
}