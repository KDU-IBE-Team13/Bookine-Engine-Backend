package com.example.ibeproject.service;

import java.sql.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.BookingConstants;
import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;

@Service
public class BillingDetailsService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

     public BillingDetailsDTO addDetails(BillingDetailsDTO billingDetailsDTO) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = BookingConstants.INSERT_CHECKOUT_DETAILS_QUERY;

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                UUID billingId = UUID.randomUUID();
                preparedStatement.setObject(1, billingId);
                preparedStatement.setBoolean(2, billingDetailsDTO.isAgreeToTerms());
                preparedStatement.setString(3, billingDetailsDTO.getBillingEmail());
                preparedStatement.setString(4, billingDetailsDTO.getBillingFirstName());
                preparedStatement.setString(5, billingDetailsDTO.getBillingLastName());
                preparedStatement.setString(6, billingDetailsDTO.getBillingPhone());
                preparedStatement.setString(7, billingDetailsDTO.getCardNumber());
                preparedStatement.setString(8, billingDetailsDTO.getCity());
                preparedStatement.setString(9, billingDetailsDTO.getCountry());
                preparedStatement.setString(10, billingDetailsDTO.getCountryState());
                preparedStatement.setInt(11, billingDetailsDTO.getExpMM());
                preparedStatement.setInt(12, billingDetailsDTO.getExpYY());
                preparedStatement.setString(13, billingDetailsDTO.getMailingAddress1());
                preparedStatement.setString(14, billingDetailsDTO.getMailingAddress2());
                preparedStatement.setBoolean(15, billingDetailsDTO.isSpecialOffers());
                preparedStatement.setString(16, billingDetailsDTO.getTravelerEmail());
                preparedStatement.setString(17, billingDetailsDTO.getTravelerFirstName());
                preparedStatement.setString(18, billingDetailsDTO.getTravelerLastName());
                preparedStatement.setString(19, billingDetailsDTO.getTravelerPhone());
                preparedStatement.setInt(20, billingDetailsDTO.getZip());
             

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    billingDetailsDTO.setBillingId(billingId);
                    return billingDetailsDTO;
                }
            }
        }

        throw new SQLException("Failed to add data. No rows affected.");
    }

}
