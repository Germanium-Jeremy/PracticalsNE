package com.utility.billing.customer;

import com.utility.billing.user.Role;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import com.utility.billing.user.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsByNationalId(request.getNationalId())) {
            throw new RuntimeException("Customer with this National ID already exists");
        }

        // Create User account for the customer so they can log in
        User user = null;
        if (request.getEmail() != null) {
            user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user == null) {
                user = User.builder()
                        .fullName(request.getFullNames())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhone())
                        .password(passwordEncoder.encode("Customer123!")) // Default password
                        .role(Role.ROLE_CUSTOMER)
                        .status(UserStatus.ACTIVE)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();
                user = userRepository.save(user);
            }
        }

        Customer customer = Customer.builder()
                .fullNames(request.getFullNames())
                .nationalId(request.getNationalId())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(CustomerStatus.ACTIVE)
                .user(user)
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

        if (request.getFullNames() != null) customer.setFullNames(request.getFullNames());
        if (request.getNationalId() != null) {
            customerRepository.findByNationalId(request.getNationalId())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) throw new RuntimeException("National ID already taken");
                    });
            customer.setNationalId(request.getNationalId());
        }
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
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
