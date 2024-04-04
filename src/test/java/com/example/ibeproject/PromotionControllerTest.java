package com.example.ibeproject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.example.ibeproject.controller.PromotionController;
import com.example.ibeproject.dto.promotions.PromotionDTO;
import com.example.ibeproject.dto.promotions.PromotionResponseDTO;
import com.example.ibeproject.service.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class PromotionControllerTest {

    @Mock
    private PromotionService promotionService;

    @InjectMocks
    private PromotionController promotionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPromotions() {
        List<PromotionDTO> expectedPromotions = new ArrayList<>();
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setPromotionId(1);
        promotionDTO.setPromotionTitle("Test Promotion");
        expectedPromotions.add(promotionDTO);

        when(promotionService.getAllPromotions()).thenReturn(expectedPromotions);

        ResponseEntity<List<PromotionDTO>> responseEntity = promotionController.getAllPromotions();

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedPromotions);
    }

    @Test
    public void testGetApplicablePromotions() {
        int tenantId = 1;
        int propertyId = 1;
        String checkInDate = "2024-04-01";
        String checkOutDate = "2024-04-10";
        boolean isSeniorCitizen = false;
        boolean isMilitaryPersonnel = false;

        PromotionResponseDTO expectedPromotionResponse = new PromotionResponseDTO();

        when(promotionService.getApplicablePromotions(tenantId, propertyId, checkInDate, checkOutDate, isSeniorCitizen, isMilitaryPersonnel)).thenReturn(expectedPromotionResponse);

        ResponseEntity<PromotionResponseDTO> responseEntity = promotionController.getApplicablePromotions(tenantId, propertyId, checkInDate, checkOutDate, isSeniorCitizen, isMilitaryPersonnel);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedPromotionResponse);
    }

}
