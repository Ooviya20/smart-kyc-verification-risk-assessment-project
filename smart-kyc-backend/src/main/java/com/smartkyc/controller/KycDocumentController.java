package com.smartkyc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartkyc.dto.KycDocumentRequest;
import com.smartkyc.dto.KycVerificationRequest;
import com.smartkyc.entity.KycDocument;
import com.smartkyc.service.KycDocumentService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/kyc-documents")
public class KycDocumentController {

    private final KycDocumentService kycDocumentService;

    public KycDocumentController(
            KycDocumentService kycDocumentService) {

        this.kycDocumentService = kycDocumentService;
    }

    // Add KYC Document
    @PostMapping
    public KycDocument saveDocument(
            @RequestBody KycDocumentRequest request) {

        return kycDocumentService.saveDocument(request);
    }

    // Get all KYC Documents
    @GetMapping
    public List<KycDocument> getAllDocuments() {

        return kycDocumentService.getAllDocuments();
    }

    // Verify KYC Document
    @PutMapping("/{id}/verify")
    public KycDocument verifyDocument(
            @PathVariable Long id,
            @RequestBody KycVerificationRequest request) {

        return kycDocumentService.verifyDocument(
                id,
                request.getStatus(),
                request.getReason()
        );
    }

    // Check Document Expiry
    @PutMapping("/{id}/check-expiry")
    public KycDocument checkDocumentExpiry(
            @PathVariable Long id) {

        return kycDocumentService.checkDocumentExpiry(id);
    }

    // Delete KYC Document
    @DeleteMapping("/{id}")
    public String deleteDocument(
            @PathVariable Long id) {

        kycDocumentService.deleteDocument(id);

        return "KYC document deleted successfully";
    }
    
    @PutMapping("/{id}/verify-pan")
    public KycDocument verifyPanAutomatically(
            @PathVariable Long id) {

        return kycDocumentService.verifyPanAutomatically(id);
    }
}