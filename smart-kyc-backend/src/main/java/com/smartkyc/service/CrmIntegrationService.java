package com.smartkyc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smartkyc.dto.CrmCustomerDto;
import com.smartkyc.dto.CrmDocumentDto;
import com.smartkyc.dto.KycDocumentRequest;
import com.smartkyc.entity.Customer;
import com.smartkyc.entity.KycDocument;
import com.smartkyc.repository.CustomerRepository;

@Service
public class CrmIntegrationService {

    private final RestTemplate restTemplate;
    private final CustomerService customerService;
    private final KycDocumentService kycDocumentService;
    private final CustomerRepository customerRepository;

    public CrmIntegrationService(
            CustomerService customerService,
            KycDocumentService kycDocumentService,
            CustomerRepository customerRepository) {

        this.restTemplate = new RestTemplate();
        this.customerService = customerService;
        this.kycDocumentService = kycDocumentService;
        this.customerRepository = customerRepository;
    }

    // Get customer from CRM
    public CrmCustomerDto getCustomerFromCrm(Long customerId) {

        String url =
                "http://localhost:8081/api/crm/customers/"
                        + customerId;

        return restTemplate.getForObject(
                url,
                CrmCustomerDto.class
        );
    }

    // Get documents from CRM
    public CrmDocumentDto[] getDocumentsFromCrm(Long customerId) {

        String url =
                "http://localhost:8081/api/crm/documents/customer/"
                        + customerId;

        return restTemplate.getForObject(
                url,
                CrmDocumentDto[].class
        );
    }

    // Verify CRM PAN
    public String verifyCrmPan(Long customerId) {

        CrmCustomerDto customer =
                getCustomerFromCrm(customerId);

        CrmDocumentDto[] documents =
                getDocumentsFromCrm(customerId);

        for (CrmDocumentDto document : documents) {

            if ("PAN".equalsIgnoreCase(
                    document.getDocumentType())) {

                if (customer.getPan() != null
                        && customer.getPan().equalsIgnoreCase(
                                document.getDocumentNumber())) {

                    customerService.updateKycStatus(
                            customerId,
                            "VERIFIED",
                            "PAN details matched successfully"
                    );

                    return "PAN VERIFIED";

                } else {

                    customerService.updateKycStatus(
                            customerId,
                            "REJECTED",
                            "PAN number does not match"
                    );

                    return "PAN REJECTED - PAN number does not match";
                }
            }
        }

        return "PAN DOCUMENT NOT FOUND";
    }

    // Compare Smart KYC customer with CRM customer
    public String compareCustomerWithCrm(
            Long smartKycCustomerId,
            Long crmCustomerId) {

        Customer smartKycCustomer =
                customerRepository.findById(
                        smartKycCustomerId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Smart KYC customer not found with id: "
                                        + smartKycCustomerId
                        )
                );

        CrmCustomerDto crmCustomer =
                getCustomerFromCrm(crmCustomerId);

        if (crmCustomer == null) {

            throw new RuntimeException(
                    "Customer not found in CRM with id: "
                            + crmCustomerId
            );
        }

        boolean nameMatch =
                isSame(
                        smartKycCustomer.getFullName(),
                        crmCustomer.getName()
                );

        boolean dobMatch =
                smartKycCustomer.getDateOfBirth() != null
                        && isSame(
                                smartKycCustomer
                                        .getDateOfBirth()
                                        .toString(),
                                crmCustomer.getDob()
                        );

        boolean addressMatch =
                isSame(
                        smartKycCustomer.getAddress(),
                        crmCustomer.getAddress()
                );

        boolean panMatch =
                isSame(
                        smartKycCustomer.getPanNumber(),
                        crmCustomer.getPan()
                );

        boolean phoneMatch =
                isSame(
                        smartKycCustomer.getPhone(),
                        crmCustomer.getPhone()
                );

        if (nameMatch
                && dobMatch
                && addressMatch
                && panMatch
                && phoneMatch) {

            customerService.updateKycStatus(
                    smartKycCustomerId,
                    "VERIFIED",
                    "CRM and Smart KYC customer details matched successfully"
            );

            return "CUSTOMER VERIFIED - All details matched";
        }

        StringBuilder mismatchReason =
                new StringBuilder();

        if (!nameMatch) {
            mismatchReason.append("Name mismatch; ");
        }

        if (!dobMatch) {
            mismatchReason.append("Date of birth mismatch; ");
        }

        if (!addressMatch) {
            mismatchReason.append("Address mismatch; ");
        }

        if (!panMatch) {
            mismatchReason.append("PAN mismatch; ");
        }

        if (!phoneMatch) {
            mismatchReason.append("Phone mismatch; ");
        }

        customerService.updateKycStatus(
                smartKycCustomerId,
                "REJECTED",
                mismatchReason.toString()
        );

        return "CUSTOMER REJECTED - "
                + mismatchReason;
    }

    // Compare values safely
    // Removes spaces and new lines before comparison
    private boolean isSame(
            String value1,
            String value2) {

        if (value1 == null || value2 == null) {
            return false;
        }

        return value1
                .replaceAll("\\s+", "")
                .equalsIgnoreCase(
                        value2.replaceAll("\\s+", "")
                );
    }

    // Import CRM document into Smart KYC
    public KycDocument importCrmDocument(
            Long smartKycCustomerId,
            Long crmCustomerId,
            Long crmDocumentId) {

        Customer customer =
                customerRepository.findById(
                        smartKycCustomerId
                ).orElseThrow(() ->
                        new RuntimeException(
                                "Smart KYC customer not found with id: "
                                        + smartKycCustomerId
                        )
                );

        CrmDocumentDto[] documents =
                getDocumentsFromCrm(crmCustomerId);

        for (CrmDocumentDto crmDocument : documents) {

            if (crmDocument.getId().equals(
                    crmDocumentId)) {

                KycDocumentRequest request =
                        new KycDocumentRequest();

                request.setCustomerId(
                        smartKycCustomerId
                );

                request.setDocumentType(
                        crmDocument.getDocumentType()
                );

                request.setDocumentNumber(
                        crmDocument.getDocumentNumber()
                );

                request.setDocumentStatus(
                        crmDocument.getDocumentStatus()
                );

                request.setVerificationStatus(
                        "PENDING"
                );

                return kycDocumentService.saveDocument(
                        request
                );
            }
        }

        throw new RuntimeException(
                "CRM document not found with id: "
                        + crmDocumentId
                        + " for CRM customer: "
                        + crmCustomerId
        );
    }
}