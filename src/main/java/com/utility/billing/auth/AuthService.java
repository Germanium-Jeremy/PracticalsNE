package com.utility.billing.auth;

import com.utility.billing.customer.Customer;
import com.utility.billing.customer.CustomerRepository;
import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.security.JwtUtils;
import com.utility.billing.user.Role;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import com.utility.billing.user.UserStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthService(
            UserRepository userRepository, 
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder, 
            JwtUtils jwtUtils, 
            AuthenticationManager authenticationManager, 
            UserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .status(UserStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Check if a customer already exists with this email but no user link
        Customer customer = customerRepository.findAll().stream()
                .filter(c -> c.getEmail() != null && c.getEmail().equalsIgnoreCase(request.getEmail()))
                .findFirst()
                .orElse(null);

        if (customer != null) {
            customer.setUser(savedUser);
            customerRepository.save(customer);
        } else {
            // Link customer automatically for regular registrations
            customer = Customer.builder()
                    .fullNames(request.getFullName())
                    .email(request.getEmail())
                    .phone(request.getPhoneNumber())
                    .nationalId("REG-" + savedUser.getId()) // Use REG prefix for self-registered
                    .status(CustomerStatus.ACTIVE)
                    .user(savedUser)
                    .build();
            customerRepository.save(customer);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .build();
    }
}
