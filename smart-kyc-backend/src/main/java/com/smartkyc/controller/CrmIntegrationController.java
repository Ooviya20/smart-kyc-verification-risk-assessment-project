package com.smartkyc.controller;

import org.springframework.web.bind.annotation.*;

import com.smartkyc.dto.CrmCustomerDto;
import com.smartkyc.dto.CrmDocumentDto;
import com.smartkyc.entity.KycDocument;
import com.smartkyc.service.CrmIntegrationService;

@RestController
@RequestMapping("/api/crm")
public class CrmIntegrationController {

    private final CrmIntegrationService crmIntegrationService;

    public CrmIntegrationController(
            CrmIntegrationService crmIntegrationService) {

        this.crmIntegrationService = crmIntegrationService;
    }

    // Get customer from CRM
    @GetMapping("/customer/{customerId}")
    public CrmCustomerDto getCustomerFromCrm(
            @PathVariable Long customerId) {

        return crmIntegrationService.getCustomerFromCrm(
                customerId
        );
    }

    // Get documents from CRM
    @GetMapping("/documents/customer/{customerId}")
    public CrmDocumentDto[] getDocumentsFromCrm(
            @PathVariable Long customerId) {

        return crmIntegrationService.getDocumentsFromCrm(
                customerId
        );
    }

    // Verify CRM PAN
    @GetMapping("/verify/{customerId}")
    public String verifyCustomerKyc(
            @PathVariable Long customerId) {

        return crmIntegrationService.verifyCrmPan(
                customerId
        );
    }

    // Compare Smart KYC customer with CRM customer
 // Verify PAN between Smart KYC and CRM
    @PutMapping("/compare/{smartKycCustomerId}/{crmCustomerId}")
    public String compareCustomerWithCrm(
            @PathVariable Long smartKycCustomerId,
            @PathVariable Long crmCustomerId) {

        return crmIntegrationService.compareCustomerWithCrm(
                smartKycCustomerId,
                crmCustomerId
        );
    }
    

    // Import CRM document into Smart KYC
    @PostMapping(
            "/import-document/{smartKycCustomerId}/{crmCustomerId}/{crmDocumentId}"
    )
    public KycDocument importCrmDocument(
            @PathVariable Long smartKycCustomerId,
            @PathVariable Long crmCustomerId,
            @PathVariable Long crmDocumentId) {

        return crmIntegrationService.importCrmDocument(
                smartKycCustomerId,
                crmCustomerId,
                crmDocumentId
        );
    }

    
   
}