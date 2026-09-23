package com.smartkyc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartkyc.entity.Customer;
import com.smartkyc.entity.PepScreening;
import com.smartkyc.repository.CustomerRepository;
import com.smartkyc.repository.PepScreeningRepository;

@Service
public class PepScreeningService {

    private final PepScreeningRepository pepScreeningRepository;
    private final CustomerRepository customerRepository;
    private final AuditLogService auditLogService;

    public PepScreeningService(
            PepScreeningRepository pepScreeningRepository,
            CustomerRepository customerRepository,
            AuditLogService auditLogService) {

        this.pepScreeningRepository = pepScreeningRepository;
        this.customerRepository = customerRepository;
        this.auditLogService = auditLogService;
    }

    public PepScreening screenCustomer(Long customerId) {

        // Find customer
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + customerId));

        // Check existing PEP screening
        PepScreening screening = pepScreeningRepository
                .findByCustomerId(customerId)
                .orElse(new PepScreening());

        /*
         * Basic PEP screening logic.
         *
         * In a real application, this would be connected
         * to an external PEP database/API.
         *
         * For our project, we are using a simple rule.
         */

        boolean pepStatus = false;

        screening.setPepStatus(pepStatus);

        if (pepStatus) {

            screening.setScreeningResult("PEP_MATCH");

            screening.setRemarks(
                    "Customer identified as Politically Exposed Person");

        } else {

            screening.setScreeningResult("CLEAR");

            screening.setRemarks(
                    "No PEP match found");
        }

        screening.setScreenedAt(LocalDateTime.now());

        screening.setCustomer(customer);

        PepScreening savedScreening =
                pepScreeningRepository.save(screening);

        // Create Audit Log
        if (pepStatus) {

            auditLogService.createLog(
                    customerId,
                    "PEP_MATCH",
                    "Customer identified as Politically Exposed Person"
            );

        } else {

            auditLogService.createLog(
                    customerId,
                    "PEP_SCREENING_COMPLETED",
                    "PEP screening completed - No PEP match found"
            );
        }

        return savedScreening;
    }

    public List<PepScreening> getAllScreenings() {

        return pepScreeningRepository.findAll();
    }
}