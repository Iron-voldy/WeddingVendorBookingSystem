package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.Admin;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AdminService {

    public Admin authenticateAdmin(String username, String password) throws IOException {
        Admin admin = FileHandler.findAdminByUsername(username);
        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }
        return null;
    }

    public List<Admin> getAllAdmins() throws IOException {
        return FileHandler.getAllAdmins();
    }

    public Admin getAdminById(String id) throws IOException {
        return FileHandler.findAdminById(id);
    }
}
