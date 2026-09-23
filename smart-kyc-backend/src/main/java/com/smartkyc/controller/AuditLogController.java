package com.smartkyc.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.smartkyc.entity.AuditLog;
import com.smartkyc.service.AuditLogService;


@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5176"
})

public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @PostMapping
    public AuditLog createLog(
            @RequestParam Long customerId,
            @RequestParam String action,
            @RequestParam String description) {

        return auditLogService.createLog(
                customerId,
                action,
                description
        );
    }

    @GetMapping("/{customerId}")
    public List<AuditLog> getCustomerLogs(
            @PathVariable Long customerId) {

        return auditLogService.getCustomerLogs(customerId);
    }
    
    @GetMapping
    public List<AuditLog> getAllLogs() {
        return auditLogService.getAllLogs();
    }
}
