package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.ibeproject.dto.header.LanguageDTO;

public class LanguageDTOTest {

    @Test
    public void testLanguageDTO() {
        LanguageDTO languageDTO = new LanguageDTO("en", "English");

        assertThat(languageDTO.getKey()).isEqualTo("en");
        assertThat(languageDTO.getLangName()).isEqualTo("English");

        languageDTO.setKey("fr");
        languageDTO.setLangName("French");

        assertThat(languageDTO.getKey()).isEqualTo("fr");
        assertThat(languageDTO.getLangName()).isEqualTo("French");

        assertThat(languageDTO.toString()).contains("key=fr");
        assertThat(languageDTO.toString()).contains("langName=French");
    }
}

