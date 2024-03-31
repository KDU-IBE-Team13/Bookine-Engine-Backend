package com.example.ibeproject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import com.example.ibeproject.constants.PromoCodeConstants;
import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.example.ibeproject.exceptions.PromoCodeLoadException;
import com.example.ibeproject.service.PromoCodeService;

@ExtendWith(MockitoExtension.class)
class PromoCodeServiceTest {

    private PromoCodeService promoCodeService;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Value("${postgres.azure.db.url}")
    private String dbUrl;

    @Value("${postgres.azure.db.user}")
    private String dbUser;

    @Value("${postgres.azure.db.password}")
    private String dbPassword;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        promoCodeService = new PromoCodeService();
        promoCodeService.setDbUrl(dbUrl);
        promoCodeService.setDbUser(dbUser);
        promoCodeService.setDbPassword(dbPassword);
    }

    @Test
    public void testGetApplicablePromoCodes() throws SQLException, PromoCodeLoadException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("promo_code_id")).thenReturn(1L);
        when(mockResultSet.getString("promo_code_title")).thenReturn("Test Promo");
        when(mockResultSet.getString("promo_code_description")).thenReturn("Test Promo Description");
        when(mockResultSet.getString("discount_type")).thenReturn("Percentage");
        when(mockResultSet.getDouble("discount_value")).thenReturn(10.0);
        when(mockResultSet.getDouble("minimum_purchase_amount")).thenReturn(100.0);
        when(mockResultSet.getBoolean("active")).thenReturn(true);
        when(mockResultSet.getString("expiration_date")).thenReturn("2025-12-31");

        List<PromoCodeDTO> expectedPromoCodes = new ArrayList<>();
        PromoCodeDTO expectedPromoCode = new PromoCodeDTO();
        expectedPromoCode.setPromoCodeId(1L);
        expectedPromoCode.setPromoCodeTitle("Test Promo");
        expectedPromoCode.setPromoCodeDescription("Test Promo Description");
        expectedPromoCode.setDiscountType("Percentage");
        expectedPromoCode.setDiscountValue(10.0);
        expectedPromoCode.setMinimumPurchaseAmount(100.0);
        expectedPromoCode.setActive(true);
        expectedPromoCode.setExpirationDate("2025-12-31");
        expectedPromoCodes.add(expectedPromoCode);

        int tenantId = 1;
        int propertyId = 1;
        String checkInDate = "2024-04-01";
        String checkOutDate = "2024-04-10";
        boolean isDisabled = false;

        List<PromoCodeDTO> actualPromoCodes = promoCodeService.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled);

        assertEquals(expectedPromoCodes.size(), actualPromoCodes.size());
        assertEquals(expectedPromoCodes.get(0).getPromoCodeId(), actualPromoCodes.get(0).getPromoCodeId());
        assertEquals(expectedPromoCodes.get(0).getPromoCodeTitle(), actualPromoCodes.get(0).getPromoCodeTitle());
    }
}
