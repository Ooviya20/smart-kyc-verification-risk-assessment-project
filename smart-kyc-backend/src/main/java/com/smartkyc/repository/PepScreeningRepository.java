package com.smartkyc.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyc.entity.PepScreening;

public interface PepScreeningRepository
        extends JpaRepository<PepScreening, Long> {

    Optional<PepScreening> findByCustomerId(Long customerId);

    long countByPepStatusTrue();
}