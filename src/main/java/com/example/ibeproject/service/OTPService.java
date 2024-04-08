package com.example.ibeproject.service;

import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    public boolean validateOTP(String billingId, String otp) throws SQLException {
        // Retrieve OTP from the database based on billingId
        String storedOTP = getOTPByBillingId(billingId);
        
        // Compare storedOTP with the provided OTP
        return otp.equals(storedOTP);
    }

    public String getOTPByBillingId(String billingId) throws SQLException {
    try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
        String selectQuery = "SELECT otp FROM otp_table WHERE billing_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, billingId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("otp");
                }
            }
        }
    }
    return null; // or throw an exception if OTP is not found
}

}

