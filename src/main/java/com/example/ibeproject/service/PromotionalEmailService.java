package com.example.ibeproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class PromotionalEmailService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    public void saveEmail(String email, boolean optedIn) throws SQLException {
        if (optedIn) {
            try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                // Check if the email already exists in the database
                if (!emailExists(connection, email)) {
                    // Email does not exist, proceed with insertion
                    String insertQuery = "INSERT INTO promotional_emails_users (email) VALUES (?)";
                    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, email);
                        preparedStatement.executeUpdate();
                    }
                } else {
                    // Email already exists, do nothing or log a message
                    System.out.println("Email already exists in the database: " + email);
                }
            }
        }
    }
    
    private boolean emailExists(Connection connection, String email) throws SQLException {
        String selectQuery = "SELECT COUNT(*) AS count FROM promotional_emails_users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false; // Return false by default if an error occurs
    }
    
}