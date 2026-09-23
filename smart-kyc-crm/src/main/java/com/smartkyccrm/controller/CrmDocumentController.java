package com.smartkyccrm.controller;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.smartkyccrm.entity.CrmCustomer;
import com.smartkyccrm.entity.CrmDocument;
import com.smartkyccrm.repository.CrmCustomerRepository;
import com.smartkyccrm.repository.CrmDocumentRepository;
import java.nio.file.StandardCopyOption;
@RestController
@RequestMapping("/api/crm/documents")
public class CrmDocumentController {

    private final CrmDocumentRepository documentRepository;
    private final CrmCustomerRepository customerRepository;

    public CrmDocumentController(
            CrmDocumentRepository documentRepository,
            CrmCustomerRepository customerRepository) {

        this.documentRepository = documentRepository;
        this.customerRepository = customerRepository;
    }

    // Upload KYC Document
    @PostMapping("/upload/{customerId}")
    public CrmDocument uploadDocument(
            @PathVariable Long customerId,
            @RequestParam("documentType") String documentType,
            @RequestParam("documentNumber") String documentNumber,
            @RequestParam("documentStatus") String documentStatus,
            @RequestParam("file") MultipartFile file) throws IOException {

        CrmCustomer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Create uploads folder
        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Save file
        String fileName = file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        // Save document details
        CrmDocument document = new CrmDocument();

        document.setDocumentType(documentType);
        document.setDocumentNumber(documentNumber);
        document.setDocumentStatus(documentStatus);
        document.setFileName(fileName);
        document.setFilePath(filePath.toString());
        document.setCustomer(customer);

        return documentRepository.save(document);
    }

    // Get all documents of a customer
    @GetMapping("/customer/{customerId}")
    public List<CrmDocument> getCustomerDocuments(
            @PathVariable Long customerId) {

        return documentRepository.findAll()
                .stream()
                .filter(document ->
                        document.getCustomer().getId().equals(customerId))
                .toList();
    }
}