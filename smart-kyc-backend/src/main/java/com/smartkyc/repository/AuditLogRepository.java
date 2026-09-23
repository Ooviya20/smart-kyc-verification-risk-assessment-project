package com.smartkyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyc.entity.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByCustomerId(Long customerId);
}
