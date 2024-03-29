package com.example.ibeproject.service;

import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.example.ibeproject.exceptions.PromoCodeLoadException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PromoCodeService {

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;
    

    public List<PromoCodeDTO> getApplicablePromoCodes(int tenantId, int propertyId, String checkInDate, String checkOutDate, Boolean isDisabled) throws PromoCodeLoadException {
        String checkInDateSubstr = checkInDate.substring(0, 10);
        String checkOutDateSubstr = checkOutDate.substring(0, 10);

        LocalDate checkIn = LocalDate.parse(checkInDateSubstr);
        LocalDate checkOut = LocalDate.parse(checkOutDateSubstr);

        List<PromoCodeDTO> applicablePromoCodes = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM promo_codes")) {

                while (resultSet.next()) {
                    PromoCodeDTO promoCode = new PromoCodeDTO();
                    promoCode.setPromoCodeId(resultSet.getLong("promo_code_id"));
                    promoCode.setPromoCodeTitle(resultSet.getString("promo_code_title"));
                    promoCode.setPromoCodeDescription(resultSet.getString("promo_code_description"));
                    promoCode.setDiscountType(resultSet.getString("discount_type"));
                    promoCode.setDiscountValue(resultSet.getDouble("discount_value"));
                    promoCode.setMinimumPurchaseAmount(resultSet.getDouble("minimum_purchase_amount"));
                    promoCode.setActive(resultSet.getBoolean("active"));
                    promoCode.setExpirationDate(resultSet.getString("expiration_date"));
                
                    if (isPromoCodeApplicable(promoCode, checkIn, checkOut)) {
                        applicablePromoCodes.add(promoCode);
                    }
                }
                
        } catch (SQLException e) {
            throw new PromoCodeLoadException("Failed to fetch postgres data from database", e);
        }

        if (!isDisabled) {
            applicablePromoCodes.removeIf(applicablePromoCode -> applicablePromoCode.getPromoCodeId() == 1);
        }

        return applicablePromoCodes;
    }

    private boolean isPromoCodeApplicable(PromoCodeDTO promoCode, LocalDate checkIn, LocalDate checkOut) {
        LocalDate expirationDate = LocalDate.parse(promoCode.getExpirationDate());

        return promoCode.isActive() &&
                LocalDate.now().isBefore(expirationDate) &&
                checkIn.compareTo(checkOut) < 0;
    }


    public PromoCodeDTO createPromoCode(PromoCodeDTO promoCodeDTO) throws PromoCodeLoadException {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String insertQuery = "INSERT INTO promo_codes (promo_code_title, promo_code_description, discount_type, discount_value, minimum_purchase_amount, active, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, promoCodeDTO.getPromoCodeTitle());
                preparedStatement.setString(2, promoCodeDTO.getPromoCodeDescription());
                preparedStatement.setString(3, promoCodeDTO.getDiscountType());
                preparedStatement.setDouble(4, promoCodeDTO.getDiscountValue());
                preparedStatement.setDouble(5, promoCodeDTO.getMinimumPurchaseAmount());
                preparedStatement.setBoolean(6, promoCodeDTO.isActive());
                preparedStatement.setString(7, promoCodeDTO.getExpirationDate());

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected == 1) {
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        long promoCodeId = generatedKeys.getLong(1);
                        promoCodeDTO.setPromoCodeId(promoCodeId);
                        return promoCodeDTO;
                    }
                }
            }
        } catch (SQLException e) {
            throw new PromoCodeLoadException("Failed to create promo code in the database", e);
        }

        throw new PromoCodeLoadException("Failed to create promo code. No rows affected.");
    }
}
