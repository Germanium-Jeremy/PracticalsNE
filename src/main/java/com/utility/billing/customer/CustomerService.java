package com.utility.billing.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByNationalId(request.getNationalId())) {
            throw new RuntimeException("Customer with this National ID already exists");
        }

        Customer customer = Customer.builder()
                .fullNames(request.getFullNames())
                .nationalId(request.getNationalId())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(CustomerStatus.ACTIVE)
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setFullNames(request.getFullNames());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setAddress(request.getAddress());
        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }

        return mapToResponse(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setFullNames(customer.getFullNames());
        response.setNationalId(customer.getNationalId());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setAddress(customer.getAddress());
        response.setStatus(customer.getStatus());
        response.setCreatedAt(customer.getCreatedAt());
        return response;
    }
}
