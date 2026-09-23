package com.smartkyccrm.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import com.smartkyccrm.entity.CrmCustomer;
import com.smartkyccrm.repository.CrmCustomerRepository;

@RestController
@RequestMapping("/api/crm/customers")
public class CrmCustomerController {

    private final CrmCustomerRepository repository;

    public CrmCustomerController(CrmCustomerRepository repository) {
        this.repository = repository;
    }

    // Create Customer
    @PostMapping
    public CrmCustomer createCustomer(@RequestBody CrmCustomer customer) {
        return repository.save(customer);
    }

    // Get All Customers
    @GetMapping
    public List<CrmCustomer> getAllCustomers() {
        return repository.findAll();
    }

    // Get Customer By ID
    @GetMapping("/{id}")
    public Optional<CrmCustomer> getCustomerById(@PathVariable Long id) {
        return repository.findById(id);
    }
}
