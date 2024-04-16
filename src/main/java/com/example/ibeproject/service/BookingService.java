package com.example.ibeproject.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.exceptions.RoomDetailsNotFoundException;
import com.example.ibeproject.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.ibeproject.constants.BookingConstants;
import com.example.ibeproject.dto.booking.BookingRequestDTO;
import com.example.ibeproject.exceptions.InsufficientRoomsException;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BookingService {


    String createBooking = GraphQLConstants.CREATE_BOOKINGS;

    String updateRoomAvailability = GraphQLConstants.UPDATE_ROOM_AVAILABILITY;

    String availableRoomsByRoomType = GraphQLConstants.ROOM_TYPE_AVAILABLE_ROOMS;

    String availableRoomsByRoomId = GraphQLConstants.GET_AVAILABILITY_ID_BY_ROOM_ID;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public BookingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Integer> getAvailableRoomDetails(String checkInDate, String checkOutDate, int roomTypeId,
            int propertyId, int rooms) {
        List<Integer> availableRooms = new ArrayList<>();
        List<Integer> availabilityIds = new ArrayList<>();

        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = String.format(availableRoomsByRoomType,
                propertyId,
                checkInDate,
                checkOutDate,
                roomTypeId);

        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode roomsNode = root.path("data").path("listRoomAvailabilities");

            Map<Integer, Integer> roomCountMap = new HashMap<>();

            for (JsonNode roomNode : roomsNode) {
                int roomId = roomNode.path("room_id").asInt();
                roomCountMap.put(roomId, roomCountMap.getOrDefault(roomId, 0) + 1);
            }

            int numberOfNights = (int) LocalDate.parse(checkOutDate.substring(0, 10)).toEpochDay()
                    - (int) LocalDate.parse(checkInDate.substring(0, 10)).toEpochDay();

            for (Map.Entry<Integer, Integer> entry : roomCountMap.entrySet()) {
                int roomId = entry.getKey();
                int availabilityCount = entry.getValue();
                if (availabilityCount == numberOfNights) {
                    availableRooms.add(roomId);
                }
            }
        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room Availability details: ", e);
        }

        if (availableRooms.size() < rooms) {
            return availableRooms;
        }
        for (int i = 0; i < rooms; i++) {
            String availableIdBody = String.format(availableRoomsByRoomId, propertyId, checkInDate, checkOutDate,
                    availableRooms.get(i));

            try {
                ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, availableIdBody,
                        headers, graphqlServerUrl);
                String responseBody = responseEntity.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);

                JsonNode availabilityNodes = root.path("data").path("listRoomAvailabilities");

                for (JsonNode availabilityNode : availabilityNodes) {
                    int availabilityId = availabilityNode.path("availability_id").asInt();
                    availabilityIds.add(availabilityId);
                }
            } catch (IOException e) {
                throw new RoomDetailsNotFoundException(
                        "Error fetching availability IDs for room IDs: " + availableRooms, e);
            }

        }

        return availabilityIds;
    }

    public Integer createBooking(String checkInDate, String checkOutDate, int adultCount, int childCount,
            int totalCost, int amountDueAtResort, String guestName, int promotionId, int propertyId) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = String.format(createBooking, checkInDate,
                checkOutDate,
                adultCount,
                childCount,
                totalCost,
                amountDueAtResort,
                guestName,
                promotionId,
                propertyId,
                1);

        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode bookingNode = root.path("data").path("createBooking");

            return bookingNode.path("booking_id").asInt();
        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room Availability details: ", e);
        }
    }

    public Integer updateRoomAvailabilities(List<Integer> roomAvailabilities, int bookingId, int rooms) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        for (int i = 0; i < roomAvailabilities.size(); i++) {
            String requestBody = String.format(updateRoomAvailability, roomAvailabilities.get(i), bookingId);

            try {
                ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                        graphqlServerUrl);
                String responseBody = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);

                if (root == null || root.path("data").isMissingNode()) {
                    throw new BadRequestException("Invalid JSON structure received from the server");
                }

                JsonNode bookingNode = root.path("data").path("updateRoomAvailability");

                if (bookingNode.isMissingNode() || bookingNode.path("availability_id").isMissingNode()) {
                    throw new BadRequestException("Availability ID not found in the response");
                }

                int availabilityId = bookingNode.path("availability_id").asInt();

                if (availabilityId != roomAvailabilities.get(i)) {
                    throw new BadRequestException(
                            "Mismatch in availability ID for room: " + roomAvailabilities.get(i));
                }

            } catch (IOException e) {
                return i;

            }
        }

        return rooms;

    }

}
    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;
  
    /**
     * Attempts to make a booking based on the provided booking request.
     *
     * @param request The booking request details.
     * @return True if booking is successful, false otherwise.
     * @throws InsufficientRoomsException if there are not enough rooms available for booking.
     */
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public boolean makeBooking(BookingRequestDTO request) {
        int availableRooms = updateAvailableRoomsAtomically(request.getRoomTypeId(), request.getNumRooms());
        
        if (availableRooms >= request.getNumRooms()) {
            insertBookingRecord(request);
            return true;
        } else {
            throw new InsufficientRoomsException("Insufficient rooms available for booking");
        }
    }   

    /**
     * Atomically updates the available rooms and retrieves the updated count.
     *
     * @param roomTypeId   The ID of the room type.
     * @param bookedRooms  The number of rooms to book.
     * @return The updated count of available rooms.
     */
    private int updateAvailableRoomsAtomically(int roomTypeId, int bookedRooms) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            connection.setAutoCommit(false);
            String selectSql = BookingConstants.SELECT_ROOM_AVAILABILITY_CONC_QUERY;
            String updateSql = BookingConstants.UPDATE_ROOM_AVAILABILITY_CONC_QUERY;
            
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                selectStatement.setInt(1, roomTypeId);
                ResultSet resultSet = selectStatement.executeQuery();
                
                int availableRooms = 0;
                if (resultSet.next()) {
                    availableRooms = resultSet.getInt("available_rooms");
                }
                

                if ((availableRooms - bookedRooms >= 0)) {
                    updateStatement.setInt(1, bookedRooms);
                    updateStatement.setInt(2, roomTypeId);
                    updateStatement.executeUpdate();
                }
                
                connection.commit(); 
                return availableRooms;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Inserts a booking record into the database.
     *
     * @param request The booking request details.
     */
    private void insertBookingRecord(BookingRequestDTO request) {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            String sql = BookingConstants.INSERT_BOOKING_CONC_QUERY;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setDate(1, java.sql.Date.valueOf(request.getCheckInDate()));
                statement.setDate(2, java.sql.Date.valueOf(request.getCheckOutDate()));
                statement.setInt(3, request.getRoomTypeId());
                statement.setInt(4, request.getNumRooms());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

