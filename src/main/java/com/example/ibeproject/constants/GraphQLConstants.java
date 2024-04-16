package com.example.ibeproject.constants;

public class GraphQLConstants {

        private GraphQLConstants() {

        }

        public static final String LIST_NIGHTLY_RATES_QUERY_STRING = "{ " +
                        "  getProperty( where: {property_id: 13} ) { " +
                        "    property_id " +
                        "    room_type { " +
                        "      room_type_id " +
                        "      room_type_name " +
                        "      room_rates { " +
                        "        room_rate { " +
                        "          basic_nightly_rate " +
                        "          date " +
                        "        } " +
                        "      } " +
                        "    } " +
                        "  } " +
                        "}";

        public static final String LIST_PROPERTIES_QUERY_STRING = "{ " +
                        "  listProperties { " +
                        "    property_id " +
                        "    property_name " +
                        "  } " +
                        "}";

        public static final String ROOM_DETAILS_QUERY_STRING = "{ " +
                        "  listRoomTypes( where: {property_id: {equals: 13}}) { " +
                        "  room_type_id " +
                        "  room_type_name " +
                        "  area_in_square_feet " +
                        "  single_bed " +
                        "  double_bed " +
                        "  max_capacity " +
                        "  property_of { " +
                        "  property_address " +
                        "  } " +
                        " } " +
                        "}";

        public static final String LIST_ROOM_RATE_ROOM_TYPE_MAPPINGS = "{" +
                        "\"query\": \"query MyQuery { listRoomRateRoomTypeMappings(where: {room_rate: {date: {gte: \\\"%3$s\\\", lt: \\\"%4$s\\\"}},"
                        +
                        "room_type: {property_id: {equals: %1$d}," +
                        "property_of: {tenant_id: {equals: %2$d}}}} take: 1000) { room_rate { basic_nightly_rate date }"
                        +
                        "room_type { room_type_name room_type_id } } }\""
                        + "}";

        public static final String LIST_AVAILABLE_ROOMS = "{ \"query\": \"query MyQuery { listRoomAvailabilities(where: {property_id: {equals: %1$d}, property: {tenant_id: {equals: %2$d}}, date: {gte: \\\"%3$s\\\", lt: \\\"%4$s\\\"}, booking_id: {equals: 0}} take: 1000) { date room_id room { room_type { room_type_name } } } }\" }";

        public static final String LIST_PROMOTIONS = "{ " +
                        "  listPromotions { " +
                        "    promotion_id " +
                        "    promotion_title " +
                        "    promotion_description " +
                        "    price_factor " +
                        "    minimum_days_of_stay " +
                        "    is_deactivated " +
                        "  } " +
                        "}";

        public static final String CREATE_BOOKINGS = "{ \"query\": \"mutation MyMutation { createBooking(data: { check_in_date: \\\"%1$s\\\", check_out_date: \\\"%2$s\\\", adult_count: %3$d, child_count: %4$d, total_cost: %5$d, amount_due_at_resort: %6$d, guest: {create: {guest_name: \\\"%7$s\\\"}}, promotion_applied: {connect: {promotion_id: %8$d}}, property_booked: {connect: {property_id: %9$d}}, booking_status: {connect: {status_id: %10$d}}}) { booking_id } }\" }";

        public static final String UPDATE_ROOM_AVAILABILITY = "{ \"query\": \"mutation MyMutation2 { updateRoomAvailability( where: {availability_id: %1$d} data: { booking: {connect: {booking_id: %2$d}}}) {availability_id, booking_id, date}}\"}";

        public static final String ROOM_TYPE_AVAILABLE_ROOMS = "{ \"query\": \"query MyQuery { listRoomAvailabilities(where: {property_id: {equals: %1$d}, date: {gte: \\\"%2$s\\\", lt: \\\"%3$s\\\"}, room: {room_type_id: {equals: %4$d}}, booking_id: {equals: 0}} take: 1000) { room_id } }\" }";

        public static final String GET_AVAILABILITY_ID_BY_ROOM_ID = "{ \"query\": \"query MyQuery { listRoomAvailabilities(where: {date: {gte: \\\"%2$s\\\", lt: \\\"%3$s\\\"}, room_id: {equals: %4$d}, property_id: {equals: %1$d}}) { availability_id } }\" }";

        public static final String LIST_ROOM_RATE_IN_A_RANGE = "{ \"query\": \"query MyQuery { listRoomRateRoomTypeMappings(where: {room_type: {property_of: {tenant_id: {equals: %1$d}}, property_id: {equals: %2$d}, room_type_id: {equals: %3$d}}, room_rate: {date: {gte: \\\"%4$s\\\", lt: \\\"%5$s\\\"}}}) { room_rate { basic_nightly_rate date } room_type { room_type_name } } }\" }";
}
