package com.example.ibeproject;

import com.example.ibeproject.controller.RatingController;
import com.example.ibeproject.dto.rating.RatingDTO;
import com.example.ibeproject.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RatingControllerTest {

    @Mock
    private RatingService ratingService;

    @InjectMocks
    private RatingController ratingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAverageRatingsForAllRoomTypes() throws SQLException {
        Map<Integer, Map<String, Double>> averageRatings = new HashMap<>();
        Map<String, Double> ratingInfo = new HashMap<>();
        ratingInfo.put("avgRating", 4.5);
        ratingInfo.put("totalRatings", 100.0);
        averageRatings.put(1, ratingInfo);

        when(ratingService.getAverageRatingsForAllRoomTypes()).thenReturn(averageRatings);

        ResponseEntity<Map<Integer, Map<String, Double>>> responseEntity = ratingController.getAverageRatingsForAllRoomTypes();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(averageRatings, responseEntity.getBody());
    }

    @Test
    public void testCreateRating() throws SQLException {
        RatingDTO ratingDTO = new RatingDTO();
        ratingDTO.setRoomTypeId(1);
        ratingDTO.setRating(4);
        ratingDTO.setReview("Great experience");
        ratingDTO.setUserId(UUID.fromString("c2df6ef5-d8b9-4a74-8006-1ca82447025a"));

        when(ratingService.createRating(any(RatingDTO.class))).thenReturn(ratingDTO);

        ResponseEntity<RatingDTO> responseEntity = ratingController.createRating(ratingDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(ratingDTO, responseEntity.getBody());
    }
}
