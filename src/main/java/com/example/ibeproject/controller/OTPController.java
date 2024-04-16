package com.example.ibeproject.controller;

import com.example.ibeproject.service.BillingDetailsService;
import com.example.ibeproject.service.OTPService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/otp")
public class OTPController {

    @Autowired
    private OTPService otpService;

    @Autowired
    private BillingDetailsService billingDetailsService;

    @PostMapping(value="/validate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> validateOTP(@RequestBody OTPValidationRequest request) throws SQLException {
        String billingId = request.getBillingId();
        String otp = request.getOtp();

        boolean isValid = otpService.validateOTP(billingId, otp);

        if (isValid) {
            // Delete the row corresponding to billingId from the billing details table
            boolean deleted = billingDetailsService.deleteBillingDetailsByBillingId(billingId);
            if (deleted) {
                return ResponseEntity.ok().body(Map.of("success", true, "message", "OTP validated successfully. Billing details deleted."));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "Failed to delete billing details."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "Invalid OTP."));
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class OTPValidationRequest {
    private String billingId;
    private String otp;

}
