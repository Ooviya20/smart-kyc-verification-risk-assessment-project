package com.smartkyc.service;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;
import com.smartkyc.service.AuditLogService;
import org.springframework.stereotype.Service;

import com.smartkyc.dto.KycDocumentRequest;
import com.smartkyc.entity.Customer;
import com.smartkyc.entity.KycDocument;
import com.smartkyc.repository.CustomerRepository;
import com.smartkyc.repository.KycDocumentRepository;

@Service
public class KycDocumentService {

    private final KycDocumentRepository kycDocumentRepository;
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    public KycDocumentService(
            KycDocumentRepository kycDocumentRepository,
            CustomerRepository customerRepository,
            AuditLogService auditLogService) {

        this.kycDocumentRepository = kycDocumentRepository;
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
    }

    public KycDocument saveDocument(KycDocumentRequest request) {

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        KycDocument document = new KycDocument();

        document.setDocumentType(request.getDocumentType());
        document.setDocumentNumber(request.getDocumentNumber());
        document.setDocumentStatus(request.getDocumentStatus());
        document.setVerificationStatus(request.getVerificationStatus());

        if (request.getExpiryDate() != null) {
            document.setExpiryDate(
                    LocalDate.parse(request.getExpiryDate())
            );
        }

        document.setUploadedAt(LocalDateTime.now());

        document.setCustomer(customer);

        return kycDocumentRepository.save(document);
    }

    public List<KycDocument> getAllDocuments() {
        return kycDocumentRepository.findAll();
    }
    public KycDocument verifyDocument(
            Long documentId,
            String status,
            String reason) {

        KycDocument document = kycDocumentRepository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KYC document not found with id: " + documentId));

        // Update document verification status
        document.setVerificationStatus(status);
        document.setVerificationReason(reason);

        // Get customer
        Customer customer = document.getCustomer();

        // Update customer KYC status
        if ("VERIFIED".equalsIgnoreCase(status)) {
            customer.setKycStatus("VERIFIED");
        } 
        else if ("REJECTED".equalsIgnoreCase(status)) {

            customer.setKycStatus("REJECTED");
        }

        customerRepository.save(customer);

        if ("VERIFIED".equalsIgnoreCase(status)) {

            auditLogService.createLog(
                    customer.getId(),
                    "KYC_VERIFIED",
                    "KYC document approved for customer "
                            + customer.getFullName()
            );

        } else if ("REJECTED".equalsIgnoreCase(status)) {

            auditLogService.createLog(
                    customer.getId(),
                    "KYC_REJECTED",
                    "KYC document rejected for customer "
                            + customer.getFullName()
            );
        }

        return kycDocumentRepository.save(document);
    }
    
    public KycDocument verifyPanAutomatically(Long documentId) {

        KycDocument document = kycDocumentRepository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KYC document not found with id: " + documentId));

        Customer customer = document.getCustomer();

        String customerPan = customer.getPanNumber();
        String documentPan = document.getDocumentNumber();

        if (customerPan != null
                && documentPan != null
                && customerPan.equalsIgnoreCase(documentPan)) {

            document.setVerificationStatus("VERIFIED");
            document.setVerificationReason(
                    "PAN number matches customer details"
            );

            customer.setKycStatus("VERIFIED");

            customerRepository.save(customer);

            auditLogService.createLog(
                    customer.getId(),
                    "KYC_VERIFIED",
                    "PAN document automatically verified for customer "
                            + customer.getFullName()
            );

        } else {

            document.setVerificationStatus("REJECTED");
            document.setVerificationReason(
                    "PAN number does not match customer details"
            );

            customer.setKycStatus("REJECTED");

            customerRepository.save(customer);

            auditLogService.createLog(
                    customer.getId(),
                    "KYC_REJECTED",
                    "PAN document automatically rejected for customer "
                            + customer.getFullName()
            );
        }

        return kycDocumentRepository.save(document);
    }
    
    public KycDocument checkDocumentExpiry(Long documentId) {

        KycDocument document = kycDocumentRepository
                .findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KYC document not found with id: " + documentId));

        if (document.getExpiryDate() != null &&
                document.getExpiryDate().isBefore(LocalDate.now())) {

            document.setDocumentStatus("EXPIRED");

        } else {

            document.setDocumentStatus("VALID");
        }

        return kycDocumentRepository.save(document);
    }
    public void deleteDocument(Long id) {

        KycDocument document = kycDocumentRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "KYC document not found with id: " + id));

        kycDocumentRepository.delete(document);
    }
}