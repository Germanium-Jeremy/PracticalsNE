package com.utility.billing.customer;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerResponse {
    private Long id;
    private String fullNames;
    private String nationalId;
    private String email;
    private String phone;
    private String address;
    private CustomerStatus status;
    private LocalDateTime createdAt;
}
