package com.example.ibeproject.controller;

import com.example.ibeproject.dto.promocode.PromoCodeDTO;
import com.example.ibeproject.exceptions.PromoCodeLoadException;
import com.example.ibeproject.service.PromoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromoCodeDTO> validatePromoCode(
            @RequestParam int tenantId,
            @RequestParam int propertyId,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam String promoCodeName,
            @RequestParam(defaultValue = "false") Boolean isDisabled
    ) throws PromoCodeLoadException {
        List<PromoCodeDTO> applicablePromoCodes = promoCodeService.getApplicablePromoCodes(tenantId, propertyId, checkInDate, checkOutDate, isDisabled);
        for(PromoCodeDTO promoCodeEle: applicablePromoCodes)
        {
            if(promoCodeEle.getPromoCodeTitle().equals(promoCodeName))
            {
                return ResponseEntity.ok(promoCodeEle);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody PromoCodeDTO promoCodeDTO) throws PromoCodeLoadException {
        PromoCodeDTO createdPromoCode = promoCodeService.createPromoCode(promoCodeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromoCode);
    }
}
