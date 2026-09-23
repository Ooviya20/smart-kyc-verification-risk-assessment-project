package com.smartkyc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyc.entity.RiskAssessment;

public interface RiskAssessmentRepository
        extends JpaRepository<RiskAssessment, Long> {

    Optional<RiskAssessment> findByCustomerId(Long customerId);

    long countByRiskLevel(String riskLevel);
}