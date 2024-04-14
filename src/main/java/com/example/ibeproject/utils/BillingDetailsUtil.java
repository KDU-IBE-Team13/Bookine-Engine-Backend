package com.example.ibeproject.utils;

import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BillingDetailsUtil {
    

    public static void setPreparedStatementParameters(PreparedStatement preparedStatement, BillingDetailsDTO billingDetailsDTO) throws SQLException {
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

        preparedStatement.setString(21, billingDetailsDTO.getRoomId());
        preparedStatement.setString(22, billingDetailsDTO.getRoomTypeName());
        preparedStatement.setString(23, billingDetailsDTO.getCheckInDate());
        preparedStatement.setString(24, billingDetailsDTO.getCheckOutDate());
        preparedStatement.setDouble(25, billingDetailsDTO.getAveragePrice());
        preparedStatement.setDouble(26, billingDetailsDTO.getSubTotal());
        preparedStatement.setDouble(27, billingDetailsDTO.getTaxes());
        preparedStatement.setDouble(28, billingDetailsDTO.getVat());
        preparedStatement.setDouble(29, billingDetailsDTO.getPackageTotal());

        preparedStatement.setInt(30, billingDetailsDTO.getAdultCount());
        preparedStatement.setInt(31, billingDetailsDTO.getTeenCount());
        preparedStatement.setInt(32, billingDetailsDTO.getKidCount());
        preparedStatement.setString(33, billingDetailsDTO.getPromotionTitle());
        preparedStatement.setString(34, billingDetailsDTO.getPromotionDescription());

    }
}