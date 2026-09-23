package com.smartkyc.dto;

public class KycApprovalRequest {

    private String status;
    private String reason;

    public KycApprovalRequest() {
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
