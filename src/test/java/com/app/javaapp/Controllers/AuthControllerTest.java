package com.app.javaapp.Controllers;

import com.app.javaapp.DTO.JwtResponse;
import com.app.javaapp.DTO.LoginRequest;
import com.app.javaapp.DTO.SignupRequest;
import com.app.javaapp.Models.Role;
import com.app.javaapp.Models.User;
import com.app.javaapp.Security.JwtUtils;
import com.app.javaapp.Services.EmailProducerService;
import com.app.javaapp.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private EmailProducerService emailProducerService;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getAuthorities()).thenReturn(Set.of(() -> "ROLE_USER"));
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");
        User user = new User("testuser", "test@example.com", "password", Set.of(Role.ROLE_USER));
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody() instanceof JwtResponse);
        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("jwt-token", jwtResponse.getToken());
        assertEquals("testuser", jwtResponse.getUsername());
    }

    @Test
    void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("newuser");
        signupRequest.setEmail("new@example.com");
        signupRequest.setPassword("password");
        signupRequest.setRoles(Set.of("USER"));
        User user = new User("newuser", "new@example.com", "password", Set.of(Role.ROLE_USER));
        when(userService.register(anyString(), anyString(), anyString(), anySet())).thenReturn(user);

        ResponseEntity<?> response = authController.registerUser(signupRequest);
        assertEquals(201, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("User registered successfully"));
        verify(emailProducerService, times(1)).sendEmailToQueue(any());
    }
}