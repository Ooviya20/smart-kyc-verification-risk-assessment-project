package com.smartkyccrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyccrm.entity.CrmCustomer;

public interface CrmCustomerRepository extends JpaRepository<CrmCustomer, Long> {

}
