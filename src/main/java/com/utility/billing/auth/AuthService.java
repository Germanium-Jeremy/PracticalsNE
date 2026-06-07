package com.utility.billing.auth;

import com.utility.billing.customer.Customer;
import com.utility.billing.customer.CustomerRepository;
import com.utility.billing.customer.CustomerStatus;
import com.utility.billing.notification.NotificationService;
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
    private final NotificationService notificationService;

    public AuthService(
            UserRepository userRepository, 
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder, 
            JwtUtils jwtUtils, 
            AuthenticationManager authenticationManager, 
            UserDetailsService userDetailsService,
            NotificationService notificationService
    ) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.notificationService = notificationService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_CUSTOMER)
                .status(UserStatus.PENDING_ACTIVATION)
                .activationToken(otp)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        // Notify user about registration and OTP
        notificationService.sendActivationEmail(savedUser.getEmail(), otp);

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

        return AuthResponse.builder()
                .message("Registration successful. Please check your email for activation OTP.")
                .build();
    }

    @Transactional
    public AuthResponse verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("User is already active");
        }

        if (user.getActivationToken() == null || !user.getActivationToken().equals(request.getOtp())) {
            throw new RuntimeException("Invalid activation code");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setActivationToken(null);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtUtils.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .message("Account activated successfully")
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
