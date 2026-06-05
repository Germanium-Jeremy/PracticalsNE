package com.utility.billing.auth;

import com.utility.billing.customer.CustomerRepository;
import com.utility.billing.security.JwtUtils;
import com.utility.billing.user.User;
import com.utility.billing.user.UserRepository;
import com.utility.billing.user.UserStatus;
import com.utility.billing.user.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;

    private JwtUtils realJwtUtils;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        realJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(realJwtUtils, "secret", "9a6111f185c74236968037307044a29a6111f185c74236968037307044a29a6111f185c74236968037307044a2");
        ReflectionTestUtils.setField(realJwtUtils, "jwtExpirationMs", 3600000L);

        authService = new AuthService(userRepository, customerRepository, passwordEncoder, realJwtUtils, authenticationManager, userDetailsService);

        registerRequest = new RegisterRequest();
        registerRequest.setFullName("John Doe");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("Password123!");
        registerRequest.setPhoneNumber("0781234567");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("Password123!");
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenSuccessful() {
        User savedUser = User.builder()
                .id(1L)
                .fullName(registerRequest.getFullName())
                .email(registerRequest.getEmail())
                .build();
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_CUSTOMER")
                .build();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenSuccessful() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("test@example.com")
                .password("password")
                .authorities("ROLE_CUSTOMER")
                .build();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertNotNull(response.getAccessToken());
        verify(authenticationManager).authenticate(any());
    }
}
