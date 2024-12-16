package com.example.collectioncard.model;

public class User {
    private String email;
    private String password;

    // Constructeur par défaut
    public User() {}

    // Getters et Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
