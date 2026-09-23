package com.smartkyc.dto;

public class DashboardResponse {

    private long totalCustomers;
    private long pendingKyc;
    private long approvedKyc;
    private long rejectedKyc;

    private long lowRisk;
    private long mediumRisk;
    private long highRisk;

    private long pepMatches;
    private long expiredDocuments;

    public DashboardResponse() {
    }

    // Total Customers
    public long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    // Pending KYC
    public long getPendingKyc() {
        return pendingKyc;
    }

    public void setPendingKyc(long pendingKyc) {
        this.pendingKyc = pendingKyc;
    }

    // Approved KYC
    public long getApprovedKyc() {
        return approvedKyc;
    }

    public void setApprovedKyc(long approvedKyc) {
        this.approvedKyc = approvedKyc;
    }

    // Rejected KYC
    public long getRejectedKyc() {
        return rejectedKyc;
    }

    public void setRejectedKyc(long rejectedKyc) {
        this.rejectedKyc = rejectedKyc;
    }

    // Low Risk
    public long getLowRisk() {
        return lowRisk;
    }

    public void setLowRisk(long lowRisk) {
        this.lowRisk = lowRisk;
    }

    // Medium Risk
    public long getMediumRisk() {
        return mediumRisk;
    }

    public void setMediumRisk(long mediumRisk) {
        this.mediumRisk = mediumRisk;
    }

    // High Risk
    public long getHighRisk() {
        return highRisk;
    }

    public void setHighRisk(long highRisk) {
        this.highRisk = highRisk;
    }

    // PEP Matches
    public long getPepMatches() {
        return pepMatches;
    }

    public void setPepMatches(long pepMatches) {
        this.pepMatches = pepMatches;
    }

    // Expired Documents
    public long getExpiredDocuments() {
        return expiredDocuments;
    }

    public void setExpiredDocuments(long expiredDocuments) {
        this.expiredDocuments = expiredDocuments;
    }
}