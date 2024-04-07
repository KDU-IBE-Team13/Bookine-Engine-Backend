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
        System.out.println("ffffffffffffffffffffffffffffffffff" + rooms);
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
