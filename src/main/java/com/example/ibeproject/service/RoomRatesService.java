package com.example.ibeproject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.dto.rates.RatesDTO;
import com.example.ibeproject.exceptions.RoomDetailsNotFoundException;
import com.example.ibeproject.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoomRatesService {

    String listRoomsRatesInRange = GraphQLConstants.LIST_ROOM_RATE_IN_A_RANGE;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RoomRatesService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RatesDTO> getPricesInRange(int tenantId, int propertyId, int roomTypeId, String checkInDate,
            String checkOutDate) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);

        String roomPricesQuery = String.format(listRoomsRatesInRange,
                tenantId,
                propertyId,
                roomTypeId,
                checkInDate,
                checkOutDate);
                System.out.println(roomPricesQuery);
        List<RatesDTO> roomsList = new ArrayList<>();
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, roomPricesQuery, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();
            System.out.println(responseBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            System.out.println(root);
            JsonNode ratesNode = root.path("data").path("listRoomRateRoomTypeMappings");
            System.out.println(ratesNode);
            for (JsonNode rateNode : ratesNode) {
                RatesDTO ratesDTO = new RatesDTO();
                ratesDTO.setBasicNightlyRate(rateNode.path("room_rate").path("basic_nightly_rate").asInt());
                ratesDTO.setRoomTypeName(rateNode.path("room_type").path("room_type_name").asText());
                ratesDTO.setDate(rateNode.path("room_rate").path("date").asText());

                roomsList.add(ratesDTO);
            }

            System.out.println("roomsList: " + roomsList);
            return roomsList;
        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room details: ", e);
        }
    }
}
