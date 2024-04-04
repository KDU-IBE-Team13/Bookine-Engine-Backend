package com.example.ibeproject.dto.promotions;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionResponseDTO {
    private List<PromotionDTO> applicablePromotions;
    PromotionDTO bestPromotion;
}
