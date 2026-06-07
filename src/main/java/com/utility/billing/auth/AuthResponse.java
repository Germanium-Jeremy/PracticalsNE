package com.utility.billing.auth;

public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String message;

    public AuthResponse() {}

    public AuthResponse(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.message = message;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static class AuthResponseBuilder {
        private String accessToken;
        private String refreshToken;
        private String message;

        public AuthResponseBuilder accessToken(String accessToken) { this.accessToken = accessToken; return this; }
        public AuthResponseBuilder refreshToken(String refreshToken) { this.refreshToken = refreshToken; return this; }
        public AuthResponseBuilder message(String message) { this.message = message; return this; }

        public AuthResponse build() {
            return new AuthResponse(accessToken, refreshToken, message);
        }
    }
}
