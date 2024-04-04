package com.example.ibeproject.dto.rating;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    private UUID ratingId;
    private int propertyId;
    private int roomTypeId;
    private int rating;
    private String review;
    private UUID userId;
    private String token;

}