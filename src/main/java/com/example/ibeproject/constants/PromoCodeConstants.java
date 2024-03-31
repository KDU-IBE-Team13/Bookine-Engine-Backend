package com.example.ibeproject.constants;

public class PromoCodeConstants {
    public static final String GET_ALL_PROMO_CODES_QUERY = "SELECT * FROM promo_codes";
    public static final String CREATE_PROMO_CODE_QUERY = "INSERT INTO promo_codes (promo_code_title, promo_code_description, discount_type, discount_value, minimum_purchase_amount, active, expiration_date) VALUES (?, ?, ?, ?, ?, ?, ?)";
}
