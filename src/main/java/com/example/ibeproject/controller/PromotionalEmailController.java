package com.example.ibeproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.ibeproject.service.PromotionalEmailService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.SQLException;

@RestController
public class PromotionalEmailController {

    @Autowired
    private PromotionalEmailService promotionalEmailService;

    @PostMapping("/api/save-email")
    public String saveEmailAndOptInStatus(@RequestBody EmailAndOptInRequest request) {
        String email = request.getEmail();
        boolean optedIn = request.isOptedIn();
        
        try {
            promotionalEmailService.saveEmail(email, optedIn);
            return "Email and opt-in status saved successfully";
        } catch (SQLException e) {
            // Log the error or handle it appropriately
            e.printStackTrace();
            return "Failed to save email and opt-in status";
        }
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class EmailAndOptInRequest {
    private String email;
    private boolean optedIn;

    // Getters and setters
}