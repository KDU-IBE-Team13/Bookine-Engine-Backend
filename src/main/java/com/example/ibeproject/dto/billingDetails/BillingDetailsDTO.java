package com.example.ibeproject.dto.billingDetails;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDetailsDTO {
    private UUID billingId;
    private boolean agreeToTerms;
    private String billingEmail;
    private String billingFirstName;
    private String billingLastName;
    private String billingPhone;
    private String cardNumber;
    private String city;
    private String country;
    private String countryState;
    private int expMM;
    private int expYY;
    private String mailingAddress1;
    private String mailingAddress2;
    private boolean specialOffers;
    private String travelerEmail;
    private String travelerFirstName;
    private String travelerLastName;
    private String travelerPhone;
    private int zip;

    private String roomId;
    private String roomTypeName;
    private String checkInDate;
    private String checkOutDate;
    private double averagePrice;
    private double subTotal;
    private double taxes;
    private double vat;
    private double packageTotal;

    private int adultCount;
    private int teenCount;
    private int kidCount;
    private String promotionTitle;
    private String promotionDescription;
    
}