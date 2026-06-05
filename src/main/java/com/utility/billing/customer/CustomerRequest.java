package com.utility.billing.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CustomerRequest {
    @NotBlank(message = "Full names are required")
    @Pattern(regexp = "^[^\\s]+\\s+[^\\s]+.*$", message = "Full name must contain at least two names (at least one space)")
    private String fullNames;

    @NotBlank(message = "National ID is required")
    @Pattern(regexp = "^\\d{16}$", message = "National ID must be exactly 16 digits")
    private String nationalId;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^(07[2389]\\d{7}|\\+2507[2389]\\d{7})$", 
            message = "Invalid phone number. Use 07... (10 digits) or +2507... (13 characters)")
    @Size(min = 10, max = 13, message = "Phone number must be between 10 and 13 characters")
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
