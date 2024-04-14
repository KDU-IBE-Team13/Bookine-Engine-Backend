package com.example.ibeproject.constants;

public class RatingConstants {
    public static final String GET_ALL_RATINGS_QUERY = "SELECT * FROM ratings WHERE room_type_id = ? AND property_id = 13";
    public static final String GET_AVG_RATING_COUNT_QUERY = "SELECT room_type_id, AVG(rating) AS avg_rating, COUNT(*) AS total_ratings FROM ratings WHERE property_id = 13 GROUP BY room_type_id";
    public static final String GET_AVG_RATING_QUERY = "SELECT AVG(rating) AS avg_rating FROM ratings WHERE room_type_id = ? AND property_id = 13";
    public static final String CREATE_RATING_QUERY = "INSERT INTO ratings (rating_id, room_type_id, rating, review, user_id, property_id) VALUES (?, ?, ?, ?, ?, 13)";
    public static final String GET_RATNGS = "SELECT * FROM ratings LIMIT ? OFFSET ?";
}
