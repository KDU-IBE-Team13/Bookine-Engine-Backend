package com.example.ibeproject.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
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

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public BookingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        // System.out.println(createBooking);
        // System.out.println(requestBody);
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

    public Integer updateRoomAvailabilities(int[] roomAvailabilities, int bookingId) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);

        for (int i = 0; i < roomAvailabilities.length; i++) {
            String requestBody = String.format(updateRoomAvailability, roomAvailabilities[i], bookingId);

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

                if (availabilityId != roomAvailabilities[i]) {
                    throw new BadRequestException(
                            "Mismatch in availability ID for room: " + roomAvailabilities[i]);
                }

            } catch (IOException e) {
                return i;

            }
        }

        return roomAvailabilities.length;

    }

}
