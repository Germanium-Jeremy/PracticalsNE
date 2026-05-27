package com.app.javaapp.Services;

import com.app.javaapp.Models.Role;
import com.app.javaapp.Models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<String, User> userByUsername = new ConcurrentHashMap<>();
    private final Map<String, User> userByEmail = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Find user by username
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(userByUsername.get(username));
    }

    // Find user by email
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userByEmail.get(email));
    }

    // Register new user
    public User register(String username, String email, String rawPassword, Set<Role> roles) {
        // Check if username or email already exists
        if (userByUsername.containsKey(username)) {
            throw new RuntimeException("Username already exists");
        }
        if (userByEmail.containsKey(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setId(idGenerator.getAndIncrement());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword)); // encrypt password
        user.setRoles(roles != null ? roles : Set.of(Role.ROLE_USER)); // default role USER

        userByUsername.put(username, user);
        userByEmail.put(email, user);
        return user;
    }

    // Helper to verify password (for login)
    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}