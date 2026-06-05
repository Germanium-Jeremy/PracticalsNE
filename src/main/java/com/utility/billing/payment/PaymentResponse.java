package com.utility.billing.payment;

import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id;
    private String billNumber;
    private Double amountPaid;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
    private String recordedBy;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBillNumber() { return billNumber; }
    public void setBillNumber(String billNumber) { this.billNumber = billNumber; }
    public Double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(Double amountPaid) { this.amountPaid = amountPaid; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public String getRecordedBy() { return recordedBy; }
    public void setRecordedBy(String recordedBy) { this.recordedBy = recordedBy; }
}
