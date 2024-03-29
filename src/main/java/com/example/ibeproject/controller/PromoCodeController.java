package com.example.ibeproject.controller;

import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.example.ibeproject.exceptions.PromoCodeLoadException;
import com.example.ibeproject.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promo-code")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @Autowired
    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping(value = "/applicable-promo-codes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PromoCodeDTO>> getApplicablePromoCodes(
            @RequestParam int tenantId,
            @RequestParam int propertyId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam(defaultValue = "false") Boolean isDisabled
    ) throws PromoCodeLoadException {
        List<PromoCodeDTO> applicablePromoCodes = promoCodeService.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled);
        return ResponseEntity.ok(applicablePromoCodes);
    }
}
