package com.example.ibeproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.ibeproject.exceptions.NightlyRateException;
import com.example.ibeproject.service.NightlyRateService;

class NightlyRateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NightlyRateService nightlyRateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMinimumNightlyRates_Success() {
        String responseBody = "{\"data\":{\"getProperty\":{\"room_type\":[{\"room_rates\":[{\"room_rate\":{\"date\":\"2024-03-17\",\"basic_nightly_rate\":100.0}}]}]}}}";
        ResponseEntity<String> mockResponseEntity = ResponseEntity.ok(responseBody);
        HttpHeaders mockHeaders = new HttpHeaders();
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(ResponseEntity.ok(responseBody));

        Map<String, Double> expectedRates = Collections.singletonMap("2024-03-17", 100.0);
        Map<String, Double> actualRates = nightlyRateService.getMinimumNightlyRates(1, 10);

        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
        assertEquals(expectedRates, actualRates);
    }

    @Test
    void getMinimumNightlyRates_RestClientException() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenThrow(RestClientException.class);

        try {
            nightlyRateService.getMinimumNightlyRates(1, 10);
        } catch (Exception e) {
            verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(String.class));
            assertEquals(NightlyRateException.class, e.getClass());
            assertEquals("Error fetching nightly rates: ", e.getMessage());
        }
    }
}
