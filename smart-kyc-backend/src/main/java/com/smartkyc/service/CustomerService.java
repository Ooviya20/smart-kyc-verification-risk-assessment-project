package com.smartkyc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.smartkyc.entity.AuditLog;
import com.smartkyc.entity.Customer;
import com.smartkyc.repository.AuditLogRepository;
import com.smartkyc.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuditLogRepository auditLogRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            AuditLogRepository auditLogRepository) {

        this.customerRepository = customerRepository;
        this.auditLogRepository = auditLogRepository;
    }

    // Create new customer
    public Customer createCustomer(Customer customer) {

        // Check duplicate PAN
        if (customer.getPanNumber() != null &&
                customerRepository
                        .findByPanNumber(customer.getPanNumber())
                        .isPresent()) {

            throw new RuntimeException("Duplicate PAN number found");
        }

        // Check duplicate Aadhaar
        if (customer.getAadhaarNumber() != null &&
                customerRepository
                        .findByAadhaarNumber(customer.getAadhaarNumber())
                        .isPresent()) {

            throw new RuntimeException("Duplicate Aadhaar number found");
        }

        // Check duplicate Email
        if (customer.getEmail() != null &&
                customerRepository
                        .findByEmail(customer.getEmail())
                        .isPresent()) {

            throw new RuntimeException("Duplicate email found");
        }

        // Check duplicate Phone
        if (customer.getPhone() != null &&
                customerRepository
                        .findByPhone(customer.getPhone())
                        .isPresent()) {

            throw new RuntimeException("Duplicate phone number found");
        }

        return customerRepository.save(customer);
    }

    // Update KYC status
    public Customer updateKycStatus(
            Long customerId,
            String status,
            String reason) {

        // Find customer
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + customerId));

        // Validate KYC status
        if (!status.equalsIgnoreCase("VERIFIED") &&
                !status.equalsIgnoreCase("REJECTED") &&
                !status.equalsIgnoreCase("MANUAL_REVIEW")) {

            throw new RuntimeException(
                    "KYC status must be VERIFIED, REJECTED or MANUAL_REVIEW");
        }

        // Update KYC status
        customer.setKycStatus(status.toUpperCase());

        Customer savedCustomer =
                customerRepository.save(customer);

        // Create automatic audit log
        AuditLog log = new AuditLog();

        log.setCustomerId(customerId);
        log.setAction("KYC_" + status.toUpperCase());
        log.setDescription(reason);
        log.setPerformedAt(LocalDateTime.now());

        auditLogRepository.save(log);

        return savedCustomer;
    }

    // Get all customers
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}