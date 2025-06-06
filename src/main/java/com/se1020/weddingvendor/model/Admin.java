package com.se1020.weddingvendor.model;

import java.io.Serializable;
import java.util.UUID;

public class Admin implements Serializable {
    private String id;
    private String username;
    private String password;
    private String email;
    private String name;

    public Admin() {
        this.id = UUID.randomUUID().toString();
    }

    public Admin(String username, String password, String email, String name) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + "," + username + "," + password + "," + email + "," + name;
    }
}
