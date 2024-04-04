package com.example.ibeproject;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.ibeproject.dto.header.CurrencyDTO;
import com.example.ibeproject.dto.header.HeaderConfigDTO;
import com.example.ibeproject.dto.header.LanguageDTO;

public class HeaderConfigDTOTest {

    @Test
    public void testHeaderConfigDTO() {
        LanguageDTO language1 = new LanguageDTO("en", "English");
        LanguageDTO language2 = new LanguageDTO("fr", "French");
        List<LanguageDTO> supportedLanguages = Arrays.asList(language1, language2);

        CurrencyDTO currency1 = new CurrencyDTO("USD", "$");
        CurrencyDTO currency2 = new CurrencyDTO("EUR", "€");
        List<CurrencyDTO> supportedCurrencies = Arrays.asList(currency1, currency2);

        HeaderConfigDTO headerConfigDTO = new HeaderConfigDTO("logo.png", "Website Title", supportedLanguages,
                supportedCurrencies);

        assertThat(headerConfigDTO.getLogo()).isEqualTo("logo.png");
        assertThat(headerConfigDTO.getTitle()).isEqualTo("Website Title");
        assertThat(headerConfigDTO.getSupportedLanguages()).containsExactly(language1, language2);
        assertThat(headerConfigDTO.getSupportedCurrencies()).containsExactly(currency1, currency2);

        headerConfigDTO.setLogo("new_logo.png");
        headerConfigDTO.setTitle("New Title");
        assertThat(headerConfigDTO.getLogo()).isEqualTo("new_logo.png");
        assertThat(headerConfigDTO.getTitle()).isEqualTo("New Title");
        assertThat(headerConfigDTO.getSupportedLanguages()).containsExactly(language1, language2);
        assertThat(headerConfigDTO.getSupportedCurrencies()).containsExactly(currency1, currency2);

        assertThat(headerConfigDTO.toString()).contains("logo=new_logo.png");
        assertThat(headerConfigDTO.toString()).contains("title=New Title");
        assertThat(headerConfigDTO.toString()).contains("supportedLanguages=[" + language1.toString() + ", " + language2.toString() + "]");
        assertThat(headerConfigDTO.toString()).contains("supportedCurrencies=[" + currency1.toString() + ", " + currency2.toString() + "]");
    }
}

