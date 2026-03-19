package com.nodo.retotecnico.dto;

public class OAuth2Response {
    private String token;
    private String message;
    private String provider;
    private String email;
    private String name;

    public OAuth2Response() {}

    public OAuth2Response(String token, String message, String provider, String email, String name) {
        this.token = token;
        this.message = message;
        this.provider = provider;
        this.email = email;
        this.name = name;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
