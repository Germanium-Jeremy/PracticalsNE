package com.app.javaapp.Controllers;

import com.app.javaapp.DTO.JwtResponse;
import com.app.javaapp.DTO.LoginRequest;
import com.app.javaapp.DTO.SignupRequest;
import com.app.javaapp.Models.Role;
import com.app.javaapp.Models.User;
import com.app.javaapp.Security.JwtUtils;
import com.app.javaapp.Services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toSet());

        // retrieve full user to get ID & email
        User user = userService.findByUsername(loginRequest.getUsername()).get();

        return ResponseEntity.ok(new JwtResponse(jwt, user.getId(), user.getUsername(), user.getEmail(), roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        // Check username & email existence is handled inside service, but we can add explicit checks here too
        Set<Role> roles = new HashSet<>();
        if (signupRequest.getRoles() == null || signupRequest.getRoles().isEmpty()) {
            roles.add(Role.ROLE_USER);
        } else {
            for (String roleStr : signupRequest.getRoles()) {
                try {
                    roles.add(Role.valueOf("ROLE_" + roleStr.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role: " + roleStr);
                }
            }
        }

        User newUser = userService.register(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getPassword(),
                roles
        );

        return ResponseEntity.ok("User registered successfully");
    }
}