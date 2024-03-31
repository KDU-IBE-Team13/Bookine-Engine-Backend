package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.ibeproject.dto.promocode.PromoCodeDTO;

public class PromoCodeDTOTest {

    @Test
    public void testPromoCodeDTO() {
        PromoCodeDTO promoCodeDTO = new PromoCodeDTO();
        promoCodeDTO.setPromoCodeId(1L);
        promoCodeDTO.setPromoCodeTitle("Test Promo");
        promoCodeDTO.setPromoCodeDescription("Test Promo Description");
        promoCodeDTO.setDiscountType("Percentage");
        promoCodeDTO.setDiscountValue(10.0);
        promoCodeDTO.setMinimumPurchaseAmount(100.0);
        promoCodeDTO.setActive(true);
        promoCodeDTO.setExpirationDate("2025-12-31");

        assertThat(promoCodeDTO.getPromoCodeId()).isEqualTo(1L);
        assertThat(promoCodeDTO.getPromoCodeTitle()).isEqualTo("Test Promo");
        assertThat(promoCodeDTO.getPromoCodeDescription()).isEqualTo("Test Promo Description");
        assertThat(promoCodeDTO.getDiscountType()).isEqualTo("Percentage");
        assertThat(promoCodeDTO.getDiscountValue()).isEqualTo(10.0);
        assertThat(promoCodeDTO.getMinimumPurchaseAmount()).isEqualTo(100.0);
        assertThat(promoCodeDTO.isActive()).isTrue();
        assertThat(promoCodeDTO.getExpirationDate()).isEqualTo("2025-12-31");

        assertThat(promoCodeDTO.toString()).contains("promoCodeId=1");
        assertThat(promoCodeDTO.toString()).contains("promoCodeTitle=Test Promo");
    }
}
