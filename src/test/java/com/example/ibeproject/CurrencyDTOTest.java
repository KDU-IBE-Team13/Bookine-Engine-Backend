package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.ibeproject.dto.header.CurrencyDTO;

public class CurrencyDTOTest {

    @Test
    public void testCurrencyDTO() {
        CurrencyDTO currencyDTO = new CurrencyDTO("USD", "$");

        assertThat(currencyDTO.getCurrency()).isEqualTo("USD");
        assertThat(currencyDTO.getCurrencySymbol()).isEqualTo("$");

        currencyDTO.setCurrency("EUR");
        currencyDTO.setCurrencySymbol("€");

        assertThat(currencyDTO.getCurrency()).isEqualTo("EUR");
        assertThat(currencyDTO.getCurrencySymbol()).isEqualTo("€");

        assertThat(currencyDTO.toString()).contains("currency=EUR");
        assertThat(currencyDTO.toString()).contains("currencySymbol=€");
    }
}
