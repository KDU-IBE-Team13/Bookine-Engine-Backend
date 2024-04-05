package com.example.ibeproject.constants;

public class BookingConstants {

    public static final String CREATE_RATING_QUERY = "INSERT INTO billingDetails (billing_id, room_type_id, rating, review, user_id, property_id) VALUES (?, ?, ?, ?, ?, 13)";
    public static final String INSERT_CHECKOUT_DETAILS_QUERY = "INSERT INTO billingDetails (billing_id, agree_to_terms, billing_email, billing_first_name, billing_last_name, billing_phone, card_number, city, country, country_state, exp_mm, exp_yy, mailing_address_1, mailing_address_2, special_offers, traveler_email, traveler_first_name, traveler_last_name, traveler_phone, zip) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


}
