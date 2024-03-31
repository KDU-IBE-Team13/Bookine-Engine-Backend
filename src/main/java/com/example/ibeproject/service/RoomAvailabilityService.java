package com.example.ibeproject.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
public class RoomAvailabilityService {

    String listRoomsQuery = GraphQLConstants.LIST_AVAILABLE_ROOMS;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RoomAvailabilityService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, Boolean> getRoomsAvailability(int tenantId, int propertyId, String checkInDate,
            String checkOutDate) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = String.format(listRoomsQuery,
                propertyId,
                tenantId,
                checkInDate,
                checkOutDate);
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);

            JsonNode roomsNode = root.path("data").path("listRoomAvailabilities");

            Map<String, Boolean> roomTypeAvailabilityMap = new HashMap<>();
            Map<Integer, Integer> roomCountMap = new HashMap<>();

            for (JsonNode roomNode : roomsNode) {
                int roomId = roomNode.path("room_id").asInt();
                roomCountMap.put(roomId, roomCountMap.getOrDefault(roomId, 0) + 1);
            }

            int numberOfNights = (int) LocalDate.parse(checkOutDate.substring(0, 10)).toEpochDay()
                    - (int) LocalDate.parse(checkInDate.substring(0, 10)).toEpochDay();

            for (Integer roomId : roomCountMap.keySet()) {
                int roomCount = roomCountMap.get(roomId);
                if (roomCount > numberOfNights) {
                    String roomTypeName = getRoomTypeNameByRoomId(roomId, roomsNode);
                    roomTypeAvailabilityMap.put(roomTypeName, true);
                }
            }

            return roomTypeAvailabilityMap;
        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room Availability details: ", e);
        }
    }

    private String getRoomTypeNameByRoomId(int roomId, JsonNode roomsNode) {
        for (JsonNode roomNode : roomsNode) {
            if (roomNode.path("room_id").asInt() == roomId) {
                return roomNode.path("room").path("room_type").path("room_type_name").asText();
            }
        }
        return null;
    }

}
