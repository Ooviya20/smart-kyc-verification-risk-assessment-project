package com.smartkyc.dto;

public class PepScreeningRequest {

    private Long customerId;

    private boolean pepStatus;

    private String screeningResult;

    private String remarks;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public boolean isPepStatus() {
        return pepStatus;
    }

    public void setPepStatus(boolean pepStatus) {
        this.pepStatus = pepStatus;
    }

    public String getScreeningResult() {
        return screeningResult;
    }

    public void setScreeningResult(String screeningResult) {
        this.screeningResult = screeningResult;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
