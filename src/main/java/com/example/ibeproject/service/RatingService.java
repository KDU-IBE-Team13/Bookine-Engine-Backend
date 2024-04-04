package com.example.ibeproject.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.RatingConstants;
import com.example.ibeproject.dto.rating.RatingDTO;

@Service
public class RatingService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    /* Retrieves average ratings for all room types.
    *
    * @return Map of room type IDs to average rating information.
    * @throws SQLException if there's an issue retrieving average ratings from the database.
    */
     public Map<Integer, Map<String, Double>> getAverageRatingsForAllRoomTypes() throws SQLException {
        Map<Integer, Map<String, Double>> averageRatings = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(RatingConstants.GET_AVG_RATING_COUNT_QUERY)) {

            while (resultSet.next()) {
                int roomTypeId = resultSet.getInt("room_type_id");
                double avgRating = resultSet.getDouble("avg_rating");
                double totalRatings = resultSet.getDouble("total_ratings");

                Map<String, Double> ratingInfo = new HashMap<>();
                ratingInfo.put("avgRating", avgRating);
                ratingInfo.put("totalRatings", totalRatings);

                averageRatings.put(roomTypeId, ratingInfo);
            }
        }

        return averageRatings;
    }

    /**
     * Creates a new rating.
     *
     * @param ratingDTO The RatingDTO object containing rating details.
     * @return RatingDTO object representing the created rating.
     * @throws SQLException if there's an issue creating the rating in the database.
     */
    public RatingDTO createRating(RatingDTO ratingDTO) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = RatingConstants.CREATE_RATING_QUERY;

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                UUID ratingId = UUID.randomUUID();
                preparedStatement.setObject(1, ratingId);
                preparedStatement.setInt(2, ratingDTO.getRoomTypeId());
                preparedStatement.setInt(3, ratingDTO.getRating());
                preparedStatement.setString(4, ratingDTO.getReview());
                preparedStatement.setObject(5, ratingDTO.getUserId());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    ratingDTO.setRatingId(ratingId);
                    return ratingDTO;
                }
            }
        }

        throw new SQLException("Failed to create rating. No rows affected.");
    }

    public boolean isValidToken(String token) {

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String query = "SELECT is_valid FROM tokens WHERE token_value = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, token);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        boolean isValid = resultSet.getBoolean("is_valid");
                        return isValid;
                    } else {
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    /**
     * Update token validity status in the database.
     *
     * @param tokenValue The value of the token to be updated.
     * @param isValid    The new validity status (true or false).
     * @throws SQLException if there's an issue updating the token validity status in the database.
     */
    public void updateTokenValidity(String tokenValue, boolean isValid) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String updateQuery = "UPDATE tokens SET is_valid = ? WHERE token_value = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                preparedStatement.setBoolean(1, isValid);
                preparedStatement.setString(2, tokenValue);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected != 1) {
                    throw new SQLException("Failed to update token validity status.");
                }
            }
        }
    }
}