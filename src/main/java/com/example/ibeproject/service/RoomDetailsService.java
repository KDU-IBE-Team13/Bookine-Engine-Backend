package com.example.ibeproject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.dto.roomdetails.RoomDetailsDTO;
import com.example.ibeproject.dto.roomdetails.RoomPricesDTO;

import com.example.ibeproject.exceptions.RoomDetailsNotFoundException;
import com.example.ibeproject.exceptions.RoomPricesNotFoundException;
import com.example.ibeproject.utils.GraphQLRequestBodyUtils;
import com.example.ibeproject.utils.HttpUtils;
import com.example.ibeproject.utils.RoomRatesUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RoomDetailsService {

    String listRoomsQuery = GraphQLConstants.ROOM_DETAILS_QUERY_STRING;

    String listRoomsNightlyPriceQuery = GraphQLConstants.LIST_ROOM_RATE_ROOM_TYPE_MAPPINGS;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public RoomDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 
     *
     * @return A list of {@link RoomDetailsDTO} objects representing the RoomDetails.
     * @throws RoomDetailsNotFoundException If an error occurs while fetching rooms.
     */
        public List<RoomDetailsDTO> getAllRooms() {
            HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
            String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(listRoomsQuery);
            List<RoomDetailsDTO> roomsList = new ArrayList<>();
            try {
                ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                        graphqlServerUrl);
                String responseBody = responseEntity.getBody();

                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(responseBody);
                JsonNode roomsNode = root.path("data").path("listRoomTypes");
                for (JsonNode roomNode : roomsNode) {
                    RoomDetailsDTO roomDetailsDTO = new RoomDetailsDTO();
                    roomDetailsDTO.setRoomTypeId(roomNode.path("room_type_id").asInt());
                    roomDetailsDTO.setRoomTypeName(roomNode.path("room_type_name").asText());
                    roomDetailsDTO.setAreaInSquareFeet(roomNode.path("area_in_square_feet").asDouble());
                    roomDetailsDTO.setSingleBed(roomNode.path("single_bed").asInt());
                    roomDetailsDTO.setDoubleBed(roomNode.path("double_bed").asInt());
                    roomDetailsDTO.setMaxCapacity(roomNode.path("max_capacity").asInt());
                    roomDetailsDTO.setPropertyAddress((roomNode.path("property_of").path("property_address")).asText());

                    roomsList.add(roomDetailsDTO);
                }
                return roomsList;
            } catch (IOException e) {
                throw new RoomDetailsNotFoundException("Error fetching Room details: ", e);
            }
        }

    public List<RoomPricesDTO> getAllRoomsPricesInRange(int tenantId, int propertyId, String checkInDate,
            String checkOutDate) {

        String roomPricesQuery = String.format(listRoomsNightlyPriceQuery,
                propertyId,
                tenantId,
                checkInDate,
                checkOutDate);

        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);

        List<RoomPricesDTO> roomsPrices = new ArrayList<>();
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, roomPricesQuery, headers,
                    graphqlServerUrl);
            String pricesResponseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(pricesResponseBody);
            JsonNode roomsPriceNode = root.path("data").path("listRoomRateRoomTypeMappings");

            for (JsonNode roomPriceNode : roomsPriceNode) {
                RoomPricesDTO roomPricesDTO = new RoomPricesDTO();
                roomPricesDTO.setRoomTypeId(roomPriceNode.path("room_type").path("room_type_id").asInt());
                roomPricesDTO.setRoomTypeName(roomPriceNode.path("room_type").path("room_type_name").asText());
                roomPricesDTO.setBasicNightlyRate(roomPriceNode.path("room_rate").path("basic_nightly_rate").asDouble());
                roomPricesDTO.setDate(roomPriceNode.path("room_rate").path("date").asText());

                roomsPrices.add(roomPricesDTO);
            }
        } catch (IOException e) {
            throw new RoomPricesNotFoundException("Error fetching room prices: ", e);
        }

        return roomsPrices;
    }

    public Map<String, Double> getRoomDetails(int tenantId, int propertyId, String checkInDate, String checkOutDate) {
        List<RoomPricesDTO> roomPrices = getAllRoomsPricesInRange(tenantId, propertyId, checkInDate, checkOutDate);
        return RoomRatesUtil.findRoomPricesByRoomTypeName(roomPrices);
    }
}