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

@Service
public class ListBookingService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    private final BillingDetailsMapper billingDetailsMapper = new BillingDetailsMapper();

    public List<BillingDetailsDTO> getDetailsByEmail(String billingEmail) throws SQLException {

        List<BillingDetailsDTO> billingDetailsList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String showBookingsQuery = BookingConstants.GET_BOOKINGS_BY_EMAIL;

            try (PreparedStatement preparedStatement = connection.prepareStatement(showBookingsQuery)) {
                preparedStatement.setObject(1, billingEmail);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        BillingDetailsDTO billingDetailsDTO = billingDetailsMapper
                                .mapResultSetToBillingDetailsDTO(resultSet);
                        billingDetailsList.add(billingDetailsDTO);
                    }
                }
            }
        }
        return billingDetailsList;
    }

}
