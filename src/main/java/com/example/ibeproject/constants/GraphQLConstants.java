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
            "\"query\": \"query MyQuery { listRoomRateRoomTypeMappings(where: {room_rate: {date: {gte: \\\"%3$s\\\", lte: \\\"%4$s\\\"}},"
            +
            "room_type: {property_id: {equals: %1$d}," +
            "property_of: {tenant_id: {equals: %2$d}}}} take: 1000) { room_rate { basic_nightly_rate date }" +
            "room_type { room_type_name room_type_id } } }\""
            + "}";

    public static final String LIST_AVAILABLE_ROOMS = 
    "{ \"query\": \"query MyQuery { listRoomAvailabilities(where: {property_id: {equals: %1$d}, property: {tenant_id: {equals: %2$d}}, date: {gte: \\\"%3$s\\\", lte: \\\"%4$s\\\"}, booking_id: {equals: 0}} take: 1000) { date room_id room { room_type { room_type_name } } } }\" }";


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

}
