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
import com.example.ibeproject.dto.property.PropertyDTO;
import com.example.ibeproject.exceptions.PropertyException;
import com.example.ibeproject.utils.GraphQLRequestBodyUtils;
import com.example.ibeproject.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PropertyService {

    String listPropertiesQuery = GraphQLConstants.LIST_PROPERTIES_QUERY_STRING;

    @Value("${graphql.connection.key}")
    private String apiKey;

    @Value("${graphql.server.url}")
    private String graphqlServerUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public PropertyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves all properties from the GraphQL server.
     *
     * @return A list of {@link PropertyDTO} objects representing the properties.
     * @throws PropertyException If an error occurs while fetching properties.
     */
    public List<PropertyDTO> getAllProperties() {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(listPropertiesQuery);
    
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers, graphqlServerUrl);
            String responseBody = responseEntity.getBody();

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode propertiesNode = root.path("data").path("listProperties");
    
            List<PropertyDTO> propertiesList = new ArrayList<>();
            for (JsonNode propertyNode : propertiesNode) {
                PropertyDTO propertyDTO = new PropertyDTO();
                propertyDTO.setPropertyId(propertyNode.path("property_id").asInt());
                propertyDTO.setPropertyName(propertyNode.path("property_name").asText());
                propertiesList.add(propertyDTO);
            }
            
            return propertiesList;
        } catch (IOException e) {
            throw new PropertyException("Error fetching properties: ", e);
        }
    }
    

}
