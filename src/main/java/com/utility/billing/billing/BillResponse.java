package com.utility.billing.billing;

import java.time.LocalDateTime;

public class BillResponse {
    private Long id;
    private String billNumber;
    private Long customerId;
    private String customerName;
    private Long meterId;
    private String meterNumber;
    private Integer billingMonth;
    private Integer billingYear;
    private Double consumption;
    private Double tariffAmount;
    private Double taxAmount;
    private Double penaltyAmount;
    private Double totalAmount;
    private Double paidAmount;
    private Double balance;
    private BillStatus status;
    private LocalDateTime generatedDate;
    private LocalDateTime approvedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Long getMeterId() { return meterId; }
    public void setMeterId(Long meterId) { this.meterId = meterId; }
    public String getMeterNumber() { return meterNumber; }
    public void setMeterNumber(String meterNumber) { this.meterNumber = meterNumber; }
    public Integer getBillingMonth() { return billingMonth; }
    public void setBillingMonth(Integer billingMonth) { this.billingMonth = billingMonth; }
    public Integer getBillingYear() { return billingYear; }
    public void setBillingYear(Integer billingYear) { this.billingYear = billingYear; }
    public Double getConsumption() { return consumption; }
    public void setConsumption(Double consumption) { this.consumption = consumption; }
    public Double getTariffAmount() { return tariffAmount; }
    public void setTariffAmount(Double tariffAmount) { this.tariffAmount = tariffAmount; }
    public Double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(Double taxAmount) { this.taxAmount = taxAmount; }
    public Double getPenaltyAmount() { return penaltyAmount; }
    public void setPenaltyAmount(Double penaltyAmount) { this.penaltyAmount = penaltyAmount; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public Double getPaidAmount() { return paidAmount; }
    public void setPaidAmount(Double paidAmount) { this.paidAmount = paidAmount; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public BillStatus getStatus() { return status; }
    public void setStatus(BillStatus status) { this.status = status; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public void setGeneratedDate(LocalDateTime generatedDate) { this.generatedDate = generatedDate; }
    public LocalDateTime getApprovedDate() { return approvedDate; }
    public void setApprovedDate(LocalDateTime approvedDate) { this.approvedDate = approvedDate; }
}
