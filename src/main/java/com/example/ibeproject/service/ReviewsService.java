package com.example.ibeproject.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.BookingConstants;
import com.example.ibeproject.constants.RatingConstants;
import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;
import com.example.ibeproject.dto.rating.RatingDTO;
import com.example.ibeproject.dto.rating.RatingResponseDTO;
import com.example.ibeproject.mapper.BillingDetailsMapper;

@Service
public class ReviewsService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    public List<RatingResponseDTO> getAllReviews(int numberOfReviews, int startIndex) throws SQLException {

        List<RatingResponseDTO> ratingDTOList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String showReviewsQuery = RatingConstants.GET_RATNGS;

            try (PreparedStatement preparedStatement = connection.prepareStatement(showReviewsQuery)) {
                preparedStatement.setInt(1, numberOfReviews);
                preparedStatement.setInt(2, startIndex);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        RatingResponseDTO ratingDTO = new RatingResponseDTO();
                        ratingDTO.setRatingId(UUID.fromString(resultSet.getString("rating_id")));
                        ratingDTO.setPropertyId(resultSet.getInt("property_id"));
                        ratingDTO.setRoomTypeId(resultSet.getInt("room_type_id"));
                        ratingDTO.setRating(resultSet.getInt("rating"));
                        ratingDTO.setReview(resultSet.getString("review"));
                        ratingDTO.setUserId(UUID.fromString(resultSet.getString("user_id")));
                        ratingDTO.setDate(resultSet.getString("created_at"));

                        ratingDTOList.add(ratingDTO);
                    }
                }
            }
        }
        return ratingDTOList;
    }

}
