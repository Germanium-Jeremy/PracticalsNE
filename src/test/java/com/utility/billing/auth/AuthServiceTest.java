package com.utility.billing.auth;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setFullName("Test User");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole(Role.ROLE_CUSTOMER);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenSuccessful() {
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userDetailsService.loadUserByUsername(any())).thenReturn(mock(UserDetails.class));
        when(jwtUtils.generateToken(any())).thenReturn("token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenSuccessful() {
        User user = new User();
        user.setEmail("test@example.com");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername(any())).thenReturn(mock(UserDetails.class));
        when(jwtUtils.generateToken(any())).thenReturn("token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("token", response.getAccessToken());
        verify(authenticationManager).authenticate(any());
    }
}
