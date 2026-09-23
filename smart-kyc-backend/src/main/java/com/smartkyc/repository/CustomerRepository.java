package com.smartkyc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyc.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByPanNumber(String panNumber);

    Optional<Customer> findByAadhaarNumber(String aadhaarNumber);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    long countByKycStatus(String kycStatus);
}