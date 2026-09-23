package com.smartkyc.dto;

public class KycVerificationRequest {

    private String status;
    private String reason;

    public KycVerificationRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}