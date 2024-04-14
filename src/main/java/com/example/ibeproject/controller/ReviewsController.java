package com.example.ibeproject.controller;

import com.example.ibeproject.dto.rating.RatingDTO;
import com.example.ibeproject.dto.rating.RatingResponseDTO;
import com.example.ibeproject.service.ReviewsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("api/v1/getAllreviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    @Autowired
    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatingResponseDTO>> getAllReviews(@RequestParam int numberOfReviews, @RequestParam int startIndex) {
        try {
            List<RatingResponseDTO> ratingDTOList = reviewsService.getAllReviews(numberOfReviews, startIndex);
            if (ratingDTOList != null) {
                return ResponseEntity.ok(ratingDTOList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
