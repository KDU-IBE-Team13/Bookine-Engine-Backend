package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.ibeproject.dto.footer.FooterConfigDTO;

public class FooterConfigDTOTest {

    @Test
    public void testFooterConfigDTO() {
        FooterConfigDTO footerConfigDTO = new FooterConfigDTO("logo.png", "Example Company");

        assertThat(footerConfigDTO.getLogo()).isEqualTo("logo.png");
        assertThat(footerConfigDTO.getCompanyName()).isEqualTo("Example Company");

        footerConfigDTO.setLogo("new_logo.png");
        footerConfigDTO.setCompanyName("New Company");

        assertThat(footerConfigDTO.getLogo()).isEqualTo("new_logo.png");
        assertThat(footerConfigDTO.getCompanyName()).isEqualTo("New Company");

        assertThat(footerConfigDTO.toString()).contains("logo=new_logo.png");
        assertThat(footerConfigDTO.toString()).contains("companyName=New Company");
    }
}
