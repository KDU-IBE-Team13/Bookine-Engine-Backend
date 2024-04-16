package com.example.ibeproject.constants;

public class BookingConstants {

    public static final String CREATE_RATING_QUERY = "INSERT INTO billingdetails (billing_id, room_type_id, rating, review, user_id, property_id) VALUES (?, ?, ?, ?, ?, 13)";
    public static final String INSERT_CHECKOUT_DETAILS_QUERY = "INSERT INTO billingdetails (billing_id, agree_to_terms, billing_email, billing_first_name, billing_last_name, billing_phone, card_number, city, country, country_state, exp_mm, exp_yy, mailing_address_1, mailing_address_2, special_offers, traveler_email, traveler_first_name, traveler_last_name, traveler_phone, zip) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_CHECKOUT_DETAILS_QUERY = "SELECT * FROM billingdetails"; 
    public static final String GET_CHECKOUT_DETAILS_BY_ID_QUERY = "SELECT * FROM billingdetails WHERE billing_id = ?";

    public static final String INSERT_CHECKOUT_DETAILS_QUERY_ALT = "INSERT INTO billings_alt (billing_id, agree_to_terms, billing_email, billing_first_name, billing_last_name, billing_phone, card_number, city, country, country_state, exp_mm, exp_yy, mailing_address_1, mailing_address_2, special_offers, traveler_email, traveler_first_name, traveler_last_name, traveler_phone, zip, room_id, room_type_name, check_in_date, check_out_date, average_price, sub_total, taxes, vat, package_total, adult_count, teen_count, kid_count, promotion_title, promotion_description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String GET_CHECKOUT_DETAILS_QUERY_ALT = "SELECT * FROM billings_alt"; 
    public static final String GET_CHECKOUT_DETAILS_BY_ID_QUERY_ALT = "SELECT * FROM billings_alt WHERE billing_id = ?";
    public static final String GET_BOOKINGS_BY_EMAIL  = "SELECT * FROM billings_alt where billing_email = ?";

    public static final String SELECT_ROOM_AVAILABILITY_CONC_QUERY = "SELECT available_rooms FROM room_availability_demo WHERE room_type_id = ? FOR UPDATE";
    public static final String UPDATE_ROOM_AVAILABILITY_CONC_QUERY = "UPDATE room_availability_demo SET available_rooms = available_rooms - ? WHERE room_type_id = ?";
    public static final String INSERT_BOOKING_CONC_QUERY = "INSERT INTO bookings_demo (check_in_date, check_out_date, room_type_id, num_rooms) VALUES (?, ?, ?, ?)";
}
