package com.utility.billing.customer;

import jakarta.validation.constraints.NotBlank;

public class CustomerRequest {
    @NotBlank(message = "Full names are required")
    private String fullNames;
    @NotBlank(message = "National ID is required")
    private String nationalId;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;

    public String getFullNames() { return fullNames; }
    public void setFullNames(String fullNames) { this.fullNames = fullNames; }
    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public CustomerStatus getStatus() { return status; }
    public void setStatus(CustomerStatus status) { this.status = status; }
}
