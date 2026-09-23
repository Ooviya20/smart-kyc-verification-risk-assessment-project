package com.smartkyc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartkyc.entity.Customer;
import com.smartkyc.entity.KycDocument;
import com.smartkyc.entity.RiskAssessment;
import com.smartkyc.repository.CustomerRepository;
import com.smartkyc.repository.KycDocumentRepository;
import com.smartkyc.repository.RiskAssessmentRepository;

@Service
public class RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final CustomerRepository customerRepository;
    private final KycDocumentRepository kycDocumentRepository;

    public RiskAssessmentService(
            RiskAssessmentRepository riskAssessmentRepository,
            CustomerRepository customerRepository,
            KycDocumentRepository kycDocumentRepository) {

        this.riskAssessmentRepository = riskAssessmentRepository;
        this.customerRepository = customerRepository;
        this.kycDocumentRepository = kycDocumentRepository;
    }

    public RiskAssessment assessRisk(Long customerId) {

        // Get customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + customerId));

        int riskScore = 0;

        StringBuilder riskReason = new StringBuilder();

        // 1. Check PAN
        if (customer.getPanNumber() == null ||
                customer.getPanNumber().isEmpty()) {

            riskScore += 30;

            riskReason.append("PAN information missing. ");
        }

        // 2. Check Aadhaar
        if (customer.getAadhaarNumber() == null ||
                customer.getAadhaarNumber().isEmpty()) {

            riskScore += 30;

            riskReason.append("Aadhaar information missing. ");
        }

        // 3. Check KYC Documents
        List<KycDocument> documents =
                kycDocumentRepository.findByCustomerId(customerId);

        for (KycDocument document : documents) {

            // Rejected document
            if ("REJECTED".equalsIgnoreCase(
                    document.getVerificationStatus())) {

                riskScore += 30;

                riskReason.append(
                        document.getDocumentType()
                        + " document rejected. ");
            }

            // Manual review
            if ("MANUAL_REVIEW".equalsIgnoreCase(
                    document.getVerificationStatus())) {

                riskScore += 20;

                riskReason.append(
                        document.getDocumentType()
                        + " requires manual review. ");
            }

            // Pending document
            if ("PENDING".equalsIgnoreCase(
                    document.getVerificationStatus())) {

                riskScore += 10;

                riskReason.append(
                        document.getDocumentType()
                        + " document pending verification. ");
            }

         // Expired document
            if (document.getExpiryDate() != null &&
                    ("DRIVING_LICENSE".equalsIgnoreCase(document.getDocumentType())
                    || "PASSPORT".equalsIgnoreCase(document.getDocumentType()))
                    && document.getExpiryDate().isBefore(LocalDate.now())) {

                riskScore += 20;

                riskReason.append(
                        document.getDocumentType()
                        + " document expired. ");
            }
        }

        // 4. Determine Risk Level
        String riskLevel;

        if (riskScore <= 30) {

            riskLevel = "LOW";

        } else if (riskScore <= 60) {

            riskLevel = "MEDIUM";

        } else {

            riskLevel = "HIGH";
        }

        // 5. Create or Update Risk Assessment
        RiskAssessment assessment =
                riskAssessmentRepository
                        .findByCustomerId(customerId)
                        .orElse(new RiskAssessment());

        assessment.setRiskScore(riskScore);

        assessment.setRiskLevel(riskLevel);

        assessment.setRiskReason(
                riskReason.length() == 0
                        ? "No major risk identified"
                        : riskReason.toString()
        );

        assessment.setAssessedAt(LocalDateTime.now());

        assessment.setCustomer(customer);

        return riskAssessmentRepository.save(assessment);
    }

    // Get all risk assessments
    public List<RiskAssessment> getAllRiskAssessments() {

        return riskAssessmentRepository.findAll();
    }
}