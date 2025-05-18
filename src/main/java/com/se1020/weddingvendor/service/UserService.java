package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    public User registerUser(String username, String password, String email, String name) throws IOException {
        // Check if username already exists
        User existingUser = FileHandler.findUserByUsername(username);
        if (existingUser != null) {
            return null; // Username already taken
        }

        // Create new user
        User newUser = new User(username, password, email, name);
        FileHandler.saveUser(newUser);
        return newUser;
    }

    public User authenticateUser(String username, String password) throws IOException {
        User user = FileHandler.findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() throws IOException {
        return FileHandler.getAllUsers();
    }

    public User getUserById(String id) throws IOException {
        return FileHandler.findUserById(id);
    }
}
