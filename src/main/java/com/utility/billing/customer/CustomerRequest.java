package com.utility.billing.customer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerRequest {
    @NotBlank(message = "Full names are required")
    private String fullNames;
    @NotBlank(message = "National ID is required")
    private String nationalId;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;
}
