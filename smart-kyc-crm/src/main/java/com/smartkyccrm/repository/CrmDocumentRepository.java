package com.smartkyccrm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartkyccrm.entity.CrmDocument;

public interface CrmDocumentRepository extends JpaRepository<CrmDocument, Long> {

}
