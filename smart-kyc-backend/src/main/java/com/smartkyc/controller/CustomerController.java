package com.smartkyc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import com.smartkyc.dto.KycApprovalRequest;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.smartkyc.entity.Customer;
import com.smartkyc.service.CustomerService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {

        try {
            return ResponseEntity.ok(
                    customerService.createCustomer(customer)
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    @PutMapping("/{id}/kyc-status")
    public ResponseEntity<?> updateKycStatus(
            @PathVariable Long id,
            @RequestBody KycApprovalRequest request) {

        try {
            return ResponseEntity.ok(
                    customerService.updateKycStatus(
                            id,
                            request.getStatus(),
                            request.getReason()
                    )
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    
}
