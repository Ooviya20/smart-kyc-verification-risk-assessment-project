package com.smartkyc.service;

import java.time.LocalDateTime;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartkyc.entity.AuditLog;
import com.smartkyc.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    

    public AuditLog createLog(
            Long customerId,
            String action,
            String description) {

        AuditLog log = new AuditLog();

        log.setCustomerId(customerId);
        log.setAction(action);
        log.setDescription(description);
        log.setPerformedAt(LocalDateTime.now());

        return auditLogRepository.save(log);
    }

    public List<AuditLog> getCustomerLogs(Long customerId) {
        return auditLogRepository.findByCustomerId(customerId);
    }
    
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}
