package com.smartkyc.service;

import org.springframework.stereotype.Service;

import com.smartkyc.dto.DashboardResponse;
import com.smartkyc.repository.CustomerRepository;
import com.smartkyc.repository.RiskAssessmentRepository;
import com.smartkyc.repository.PepScreeningRepository;
import com.smartkyc.repository.KycDocumentRepository;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final RiskAssessmentRepository riskAssessmentRepository;
    private final PepScreeningRepository pepScreeningRepository;
    private final KycDocumentRepository kycDocumentRepository;

    public DashboardService(
            CustomerRepository customerRepository,
            RiskAssessmentRepository riskAssessmentRepository,
            PepScreeningRepository pepScreeningRepository,
            KycDocumentRepository kycDocumentRepository) {

        this.customerRepository = customerRepository;
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.pepScreeningRepository = pepScreeningRepository;
        this.kycDocumentRepository = kycDocumentRepository;
    }

    public DashboardResponse getDashboardData() {

        DashboardResponse response = new DashboardResponse();

        // KYC counts
        response.setTotalCustomers(
                customerRepository.count()
        );

        response.setPendingKyc(
                customerRepository.countByKycStatus("PENDING")
        );

        response.setApprovedKyc(
                customerRepository.countByKycStatus("APPROVED")
        );

        response.setRejectedKyc(
                customerRepository.countByKycStatus("REJECTED")
        );

        // Risk counts
        response.setLowRisk(
                riskAssessmentRepository.countByRiskLevel("LOW")
        );

        response.setMediumRisk(
                riskAssessmentRepository.countByRiskLevel("MEDIUM")
        );

        response.setHighRisk(
                riskAssessmentRepository.countByRiskLevel("HIGH")
        );

        // PEP count
        response.setPepMatches(
                pepScreeningRepository.countByPepStatusTrue()
        );

        // Expired document count
        response.setExpiredDocuments(
                kycDocumentRepository.countByDocumentStatus("EXPIRED")
        );

        return response;
    }
}