package com.app.javaapp.DTO;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    // getters & setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}