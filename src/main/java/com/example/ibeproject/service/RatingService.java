package com.example.ibeproject.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.dto.rating.RatingDTO;

@Service
public class RatingService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    public List<RatingDTO> getRatingsByRoomTypeId(int roomTypeId) throws SQLException {
        List<RatingDTO> ratings = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ratings WHERE room_type_id = ? AND property_id = 13")) {
            preparedStatement.setInt(1, roomTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    RatingDTO rating = new RatingDTO();
                    rating.setRatingId(UUID.fromString(resultSet.getString("rating_id")));
                    rating.setRoomTypeId(resultSet.getInt("room_type_id"));
                    rating.setRating(resultSet.getInt("rating"));
                    rating.setReview(resultSet.getString("review"));
                    rating.setUserId(UUID.fromString(resultSet.getString("user_id")));
                    ratings.add(rating);
                }
            }
        }

        return ratings;
    }

    public Double getAverageRatingByRoomTypeId(int roomTypeId) throws SQLException {
        Double averageRating = null;

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT AVG(rating) AS avg_rating FROM ratings WHERE room_type_id = ? AND property_id = 13")) {
            preparedStatement.setInt(1, roomTypeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    averageRating = resultSet.getDouble("avg_rating");
                }
            }
        }

        return averageRating;
    }

     public Map<Integer, Map<String, Double>> getAverageRatingsForAllRoomTypes() throws SQLException {
        Map<Integer, Map<String, Double>> averageRatings = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT room_type_id, AVG(rating) AS avg_rating, COUNT(*) AS total_ratings FROM ratings WHERE property_id = 13 GROUP BY room_type_id")) {

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

    public RatingDTO createRating(RatingDTO ratingDTO) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = "INSERT INTO ratings (rating_id, room_type_id, rating, review, user_id, property_id) VALUES (?, ?, ?, ?, ?, 13)";

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
}
