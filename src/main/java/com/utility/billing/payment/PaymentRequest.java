package com.utility.billing.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {
    @NotNull(message = "Bill ID is required")
    private Long billId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod method;

    public Long getBillId() { return billId; }
    public void setBillId(Long billId) { this.billId = billId; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public PaymentMethod getMethod() { return method; }
    public void setMethod(PaymentMethod method) { this.method = method; }
}
