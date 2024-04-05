package com.example.ibeproject.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.BookingConstants;
import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;
import com.example.ibeproject.mapper.BillingDetailsMapper;
import com.example.ibeproject.utils.BillingDetailsUtil;

@Service
public class BillingDetailsService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    private final BillingDetailsMapper billingDetailsMapper = new BillingDetailsMapper();

    /**
     * Adds billing details to the database.
     *
     * @param billingDetailsDTO The BillingDetailsDTO object containing billing details to be added.
     * @return The BillingDetailsDTO object with the generated billing ID if successful.
     * @throws SQLException If a database access error occurs.
     */
    public BillingDetailsDTO addDetails(BillingDetailsDTO billingDetailsDTO) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = BookingConstants.INSERT_CHECKOUT_DETAILS_QUERY;

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                UUID billingId = UUID.randomUUID();
                preparedStatement.setObject(1, billingId);
                BillingDetailsUtil.setPreparedStatementParameters(preparedStatement, billingDetailsDTO);
             
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    billingDetailsDTO.setBillingId(billingId);
                    return billingDetailsDTO;
                }
            }
        }

        throw new SQLException("Failed to add data. No rows affected.");
    }

    /**
     * Retrieves billing details from the database by ID.
     *
     * @param billingId The UUID of the billing details to retrieve.
     * @return The BillingDetailsDTO object containing the requested billing details if found, null otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public BillingDetailsDTO getDetailsById(UUID billingId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String selectQuery = BookingConstants.GET_CHECKOUT_DETAILS_BY_ID_QUERY;
    
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setObject(1, billingId);
    
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return billingDetailsMapper.mapResultSetToBillingDetailsDTO(resultSet);        
                    }
                }
            }
        }
        return null;
    }
    

}
