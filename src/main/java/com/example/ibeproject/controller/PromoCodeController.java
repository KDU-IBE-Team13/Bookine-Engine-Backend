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

     /**
     * Endpoint to validate a promo code.
     *
     * @param tenantId      The ID of the tenant.
     * @param propertyId    The ID of the property.
     * @param checkInDate   The check-in date in YYYY-MM-DD format.
     * @param checkOutDate  The check-out date in YYYY-MM-DD format.
     * @param promoCodeName The name of the promo code to validate.
     * @param isDisabled    Flag indicating if the promo code is disabled (default is false).
     * @return ResponseEntity containing the PromoCodeDTO if found, else returns 404 NOT_FOUND.
     * @throws PromoCodeLoadException if there's an issue loading promo codes.
     */
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

     /**
     * Endpoint to get applicable promo codes.
     *
     * @param tenantId      The ID of the tenant.
     * @param propertyId    The ID of the property.
     * @param checkInDate   The check-in date in YYYY-MM-DD format.
     * @param checkOutDate  The check-out date in YYYY-MM-DD format.
     * @param isDisabled    Flag indicating if disabled promo codes should be included (default is false).
     * @return ResponseEntity containing a list of applicable PromoCodeDTO objects.
     * @throws PromoCodeLoadException if there's an issue loading promo codes.
     */
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


    /**
     * Endpoint to create a new promo code.
     *
     * @param promoCodeDTO The PromoCodeDTO object containing promo code details.
     * @return ResponseEntity containing the created PromoCodeDTO.
     * @throws PromoCodeLoadException if there's an issue creating the promo code.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PromoCodeDTO> createPromoCode(@RequestBody PromoCodeDTO promoCodeDTO) throws PromoCodeLoadException {
        PromoCodeDTO createdPromoCode = promoCodeService.createPromoCode(promoCodeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPromoCode);
    }
}
