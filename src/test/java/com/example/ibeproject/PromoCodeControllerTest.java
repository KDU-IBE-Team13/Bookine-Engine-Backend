package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.ibeproject.controller.PromoCodeController;
import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.example.ibeproject.exceptions.PromoCodeLoadException;
import com.example.ibeproject.service.PromoCodeService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PromoCodeControllerTest {

    @Mock
    private PromoCodeService promoCodeService;

    @InjectMocks
    private PromoCodeController promoCodeController;

    @Test
    public void testValidatePromoCode() throws PromoCodeLoadException {
        int tenantId = 1;
        int propertyId = 1;
        String checkInDate = "2024-04-01";
        String checkOutDate = "2024-04-10";
        String promoCodeName = "TESTCODE";
        boolean isDisabled = false;

        PromoCodeDTO promoCodeDTO = new PromoCodeDTO();
        promoCodeDTO.setPromoCodeId(1L);
        promoCodeDTO.setPromoCodeTitle(promoCodeName);

        List<PromoCodeDTO> applicablePromoCodes = new ArrayList<>();
        applicablePromoCodes.add(promoCodeDTO);

        when(promoCodeService.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled)).thenReturn(applicablePromoCodes);

        ResponseEntity<PromoCodeDTO> responseEntity = promoCodeController.validatePromoCode(tenantId, propertyId, checkInDate, checkOutDate, promoCodeName, isDisabled);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(promoCodeDTO);
    }

    @Test
    public void testGetApplicablePromoCodes() throws PromoCodeLoadException {
        int tenantId = 1;
        int propertyId = 1;
        String checkInDate = "2024-04-01";
        String checkOutDate = "2024-04-10";
        boolean isDisabled = false;

        PromoCodeDTO promoCodeDTO = new PromoCodeDTO();
        promoCodeDTO.setPromoCodeId(1L);
        promoCodeDTO.setPromoCodeTitle("TESTCODE");

        List<PromoCodeDTO> applicablePromoCodes = new ArrayList<>();
        applicablePromoCodes.add(promoCodeDTO);

        when(promoCodeService.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled)).thenReturn(applicablePromoCodes);

        ResponseEntity<List<PromoCodeDTO>> responseEntity = promoCodeController.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).containsExactly(promoCodeDTO);
    }

}

