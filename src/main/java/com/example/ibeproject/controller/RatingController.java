package com.example.ibeproject.controller;

import com.example.ibeproject.dto.rating.RatingDTO;
import com.example.ibeproject.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/rating")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping(value="/{roomTypeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingDTO>> getRatingsByRoomTypeId(@PathVariable int roomTypeId) {
        try {
            List<RatingDTO> ratings = ratingService.getRatingsByRoomTypeId(roomTypeId);
            return ResponseEntity.ok(ratings);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value="/average", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<Integer, Map<String, Double>>> getAverageRatingsForAllRoomTypes() {
        try {
            Map<Integer, Map<String, Double>> averageRatings = ratingService.getAverageRatingsForAllRoomTypes();
            return ResponseEntity.ok(averageRatings);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RatingDTO> createRating(@RequestBody RatingDTO ratingDTO) {
        try {
            RatingDTO createdRating = ratingService.createRating(ratingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRating);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
