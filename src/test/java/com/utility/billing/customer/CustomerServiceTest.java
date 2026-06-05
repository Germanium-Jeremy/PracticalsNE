package com.utility.billing.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private CustomerRequest customerRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRequest = new CustomerRequest();
        customerRequest.setFullNames("John Doe");
        customerRequest.setNationalId("1199080012345678");

        customer = Customer.builder()
                .id(1L)
                .fullNames("John Doe")
                .nationalId("1199080012345678")
                .status(CustomerStatus.ACTIVE)
                .build();
    }

    @Test
    void createCustomer_ShouldReturnResponse_WhenSuccessful() {
        when(customerRepository.existsByNationalId(any())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(customer);

        CustomerResponse response = customerService.createCustomer(customerRequest);

        assertNotNull(response);
        assertEquals("John Doe", response.getFullNames());
        verify(customerRepository).save(any());
    }

    @Test
    void createCustomer_ShouldThrowException_WhenNationalIdExists() {
        when(customerRepository.existsByNationalId(any())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> customerService.createCustomer(customerRequest));
    }
}
