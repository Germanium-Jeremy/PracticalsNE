package com.app.javaapp.Services;

import com.app.javaapp.Models.Role;
import com.app.javaapp.Models.User;
import com.app.javaapp.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        User user = new User("testuser", "testuser@gmail.com", "password123", Set.of(Role.ROLE_USER));
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("testuser");
        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testFindByEmail() {
        User user = new User("testuser", "testuser@gmail.com", "password123", Set.of(Role.ROLE_USER));
        when(userRepository.findByEmail("testuser@gmail.com")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByEmail("testuser@gmail.com");
        assertTrue(result.isPresent());
        assertEquals("testuser@gmail.com", result.get().getEmail());
    }

    @Test
    void testRegisterSuccess() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        // Make save return the saved entity (echo)
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.register("newuser", "new@example.com", "plainPassword", Set.of(Role.ROLE_USER));

        assertNotNull(created);
        assertEquals("newuser", created.getUsername());
        assertEquals("new@example.com", created.getEmail());
        // Password should be encoded (not equal to raw)
        assertNotEquals("plainPassword", created.getPassword());
        assertTrue(userService.matchesPassword("plainPassword", created.getPassword()));
    }

    @Test
    void testRegisterUsernameExists() {
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.register("existinguser", "email@example.com", "password", Set.of(Role.ROLE_USER)));
        assertTrue(ex.getMessage().toLowerCase().contains("username"));
    }

    @Test
    void testRegisterEmailExists() {
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.register("user", "existing@example.com", "password", Set.of(Role.ROLE_USER)));
        assertTrue(ex.getMessage().toLowerCase().contains("email"));
    }

    @Test
    void testMatchesPassword() {
        // register to get an encoded password
        when(userRepository.existsByUsername("puser")).thenReturn(false);
        when(userRepository.existsByEmail("puser@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.register("puser", "puser@example.com", "secret", Set.of(Role.ROLE_USER));
        assertTrue(userService.matchesPassword("secret", created.getPassword()));
        assertFalse(userService.matchesPassword("wrong", created.getPassword()));
    }
}
