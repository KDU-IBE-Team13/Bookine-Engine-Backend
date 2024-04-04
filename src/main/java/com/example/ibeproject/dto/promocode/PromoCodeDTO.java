package com.example.ibeproject.dto.promocode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO {
    private Long promoCodeId;
    private String promoCodeTitle;
    private String promoCodeDescription;
    private String discountType;
    private double discountValue;
    private double minimumPurchaseAmount;
    private boolean active;
    private String expirationDate; 
    
}
