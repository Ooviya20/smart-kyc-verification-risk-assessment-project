package com.smartkyc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyc.entity.KycDocument;

public interface KycDocumentRepository
        extends JpaRepository<KycDocument, Long> {

    long countByDocumentStatus(String documentStatus);

    List<KycDocument> findByCustomerId(Long customerId);
}