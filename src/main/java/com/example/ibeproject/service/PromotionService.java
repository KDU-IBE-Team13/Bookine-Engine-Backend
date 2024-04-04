package com.example.ibeproject.service;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.ibeproject.constants.GraphQLConstants;
import com.example.ibeproject.dto.promotions.PromotionDTO;
import com.example.ibeproject.dto.promotions.PromotionResponseDTO;
import com.example.ibeproject.exceptions.RoomDetailsNotFoundException;
import com.example.ibeproject.utils.GraphQLRequestBodyUtils;
import com.example.ibeproject.utils.HttpUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class PromotionService {
    String listPromotionsQuery = GraphQLConstants.LIST_PROMOTIONS;
    @Value("${graphql.connection.key}")
    private String apiKey;
    @Value("${graphql.server.url}")
    private String graphqlServerUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public PromotionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * Retrieves all promotions from the GraphQL server.
     *
     * @return List of PromotionDTO objects representing all promotions.
     * @throws RoomDetailsNotFoundException if an error occurs while fetching promotions.
     */
    public List<PromotionDTO> getAllPromotions() {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(listPromotionsQuery);
        List<PromotionDTO> promotionsList = new ArrayList<>();
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode promotionsNode = root.path("data").path("listPromotions");
            for (JsonNode promotionNode : promotionsNode) {
                PromotionDTO promotionDTO = new PromotionDTO();
                promotionDTO.setPromotionId(promotionNode.path("promotion_id").asInt());
                promotionDTO.setPromotionTitle(promotionNode.path("promotion_title").asText());
                promotionDTO.setPromotionDescription(promotionNode.path("promotion_description").asText());
                promotionDTO.setPriceFactor(promotionNode.path("price_factor").asDouble());
                promotionDTO.setMinimumDaysOfStay(promotionNode.path("minimum_days_of_stay").asInt());
                promotionDTO.setDeactivated(promotionNode.path("is_deactivated").asBoolean());
                promotionsList.add(promotionDTO);
            }
            return promotionsList;
        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room details: ", e);
        }
    }


    /**
     * Retrieves the promotion with the highest price factor from the given list of promotions.
     *
     * @param promotionsListCopy List of PromotionDTO objects to search for the highest price factor promotion.
     * @return The PromotionDTO object with the highest price factor.
     */
    public PromotionDTO getHighestPriceFactorPromotion(List<PromotionDTO> promotionsListCopy) {
        Optional<PromotionDTO> highestPricePromotion = promotionsListCopy.stream()
                .max(Comparator.comparingDouble(PromotionDTO::getPriceFactor));
        
        return highestPricePromotion.orElse(null);
    }


    /**
     * Retrieves applicable promotions based on tenant, property, dates, and eligibility criteria.
     *
     * @param tenantId          The ID of the tenant.
     * @param propertyId        The ID of the property.
     * @param checkInDate       The check-in date in YYYY-MM-DD format.
     * @param checkOutDate      The check-out date in YYYY-MM-DD format.
     * @param isSeniorCitizen   Flag indicating if the person is a senior citizen.
     * @param isMilitaryPersonnel Flag indicating if the person is a military personnel.
     * @return PromotionResponseDTO object containing applicable promotions and the best promotion.
     * @throws RoomDetailsNotFoundException if an error occurs while fetching promotions.
     */
    public PromotionResponseDTO getApplicablePromotions(int tenantId, int propertyId, String checkInDate, String checkOutDate, Boolean isSeniorCitizen, boolean isMilitaryPersonnel) {
        HttpHeaders headers = HttpUtils.createHttpHeaders(apiKey);
        String requestBody = GraphQLRequestBodyUtils.buildQueryRequestBody(listPromotionsQuery);
        List<PromotionDTO> promotionsList = new ArrayList<>();
        try {
            ResponseEntity<String> responseEntity = HttpUtils.makeHttpRequest(restTemplate, requestBody, headers,
                    graphqlServerUrl);
            String responseBody = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode promotionsNode = root.path("data").path("listPromotions");
            for (JsonNode promotionNode : promotionsNode) {
                PromotionDTO promotionDTO = new PromotionDTO();
                promotionDTO.setPromotionId(promotionNode.path("promotion_id").asInt());
                promotionDTO.setPromotionTitle(promotionNode.path("promotion_title").asText());
                promotionDTO.setPromotionDescription(promotionNode.path("promotion_description").asText());
                promotionDTO.setPriceFactor(promotionNode.path("price_factor").asDouble());
                promotionDTO.setMinimumDaysOfStay(promotionNode.path("minimum_days_of_stay").asInt());
                promotionDTO.setDeactivated(promotionNode.path("is_deactivated").asBoolean());
                promotionsList.add(promotionDTO);
            }


            if(!isSeniorCitizen)
            {
                promotionsList.removeIf(promotion -> promotion.getPromotionId() == 1);
            }

            if(!isMilitaryPersonnel)
            {
                promotionsList.removeIf(promotion -> promotion.getPromotionId() == 4);
            }


            String checkInDateSubstr = checkInDate.substring(0, 10);
            String checkOutDateSubstr = checkOutDate.substring(0, 10);

            LocalDate checkInDateTime = LocalDate.parse(checkInDateSubstr);
            LocalDate checkOutDateTime = LocalDate.parse(checkOutDateSubstr);

            long diffInDays = ChronoUnit.DAYS.between(checkInDateTime, checkOutDateTime);

            boolean includesWeekend = false;
            LocalDate tempDate = checkInDateTime;
            while (!tempDate.isAfter(checkOutDateTime)) {
                if (tempDate.getDayOfWeek() == DayOfWeek.SATURDAY || tempDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    includesWeekend = true;
                    break;
                }
                tempDate = tempDate.plusDays(1);
            }
            if(!includesWeekend)
            {
                promotionsList.removeIf(promotion -> promotion.getPromotionId() == 6);
                promotionsList.removeIf(promotion -> promotion.getPromotionId() == 3);
            }

            List<PromotionDTO> promotionsListCopy = new ArrayList<>();

            for(PromotionDTO promotionEle: promotionsList)
            {
                if((promotionEle.getMinimumDaysOfStay() < diffInDays + 1) && (!promotionEle.isDeactivated()))
                {
                    promotionsListCopy.add(promotionEle);
                }
            }

            PromotionDTO highestPricePromotion = getHighestPriceFactorPromotion(promotionsListCopy);

            PromotionResponseDTO promotionResponse = new PromotionResponseDTO();
            promotionResponse.setApplicablePromotions(promotionsListCopy);
            promotionResponse.setBestPromotion(highestPricePromotion);

            return promotionResponse;

        } catch (IOException e) {
            throw new RoomDetailsNotFoundException("Error fetching Room details: ", e);
        }
    }
}