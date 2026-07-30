package com.nodo.retotecnico.model;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private Date expiryDate;

    @OneToOne
    private User user;

    public PasswordResetToken() {}

    public PasswordResetToken(User user, String token, Date expiryDate) {
        this.user = user;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    // Getters y setters opcionales
    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

