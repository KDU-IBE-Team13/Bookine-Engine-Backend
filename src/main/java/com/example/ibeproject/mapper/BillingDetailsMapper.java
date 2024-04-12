package com.example.ibeproject.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import com.example.ibeproject.dto.billingDetails.BillingDetailsDTO;

public class BillingDetailsMapper {

    /**
     * Maps a ResultSet to a BillingDetailsDTO object.
     *
     * @param resultSet The ResultSet containing billing details data.
     * @return The BillingDetailsDTO object mapped from the ResultSet.
     * @throws SQLException If a database access error occurs.
     */
    public BillingDetailsDTO mapResultSetToBillingDetailsDTO(ResultSet resultSet) throws SQLException {
        BillingDetailsDTO billingDetailsDTO = new BillingDetailsDTO();
        billingDetailsDTO.setBillingId(UUID.fromString(resultSet.getString("billing_id")));
        billingDetailsDTO.setAgreeToTerms(resultSet.getBoolean("agree_to_terms"));
        billingDetailsDTO.setBillingEmail(resultSet.getString("billing_email"));
        billingDetailsDTO.setBillingFirstName(resultSet.getString("billing_first_name"));
        billingDetailsDTO.setBillingLastName(resultSet.getString("billing_last_name"));
        billingDetailsDTO.setBillingPhone(resultSet.getString("billing_phone"));
        billingDetailsDTO.setCardNumber(resultSet.getString("card_number"));
        billingDetailsDTO.setCity(resultSet.getString("city"));
        billingDetailsDTO.setCountry(resultSet.getString("country"));
        billingDetailsDTO.setCountryState(resultSet.getString("country_state"));
        billingDetailsDTO.setExpMM(resultSet.getInt("exp_mm"));
        billingDetailsDTO.setExpYY(resultSet.getInt("exp_yy"));
        billingDetailsDTO.setMailingAddress1(resultSet.getString("mailing_address_1"));
        billingDetailsDTO.setMailingAddress2(resultSet.getString("mailing_address_2"));
        billingDetailsDTO.setSpecialOffers(resultSet.getBoolean("special_offers"));
        billingDetailsDTO.setTravelerEmail(resultSet.getString("traveler_email"));
        billingDetailsDTO.setTravelerFirstName(resultSet.getString("traveler_first_name"));
        billingDetailsDTO.setTravelerLastName(resultSet.getString("traveler_last_name"));
        billingDetailsDTO.setTravelerPhone(resultSet.getString("traveler_phone"));
        billingDetailsDTO.setZip(resultSet.getInt("zip"));
        return billingDetailsDTO;
    }
}
