package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.Vendor;
import com.se1020.weddingvendor.util.FileHandler;
import com.se1020.weddingvendor.util.VendorLinkedList;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class VendorService {

    public Vendor createVendor(String name, String imageUrl, String description, String category, String contactInfo) throws IOException {
        Vendor vendor = new Vendor(name, imageUrl, description, category, contactInfo);
        FileHandler.saveVendor(vendor);
        return vendor;
    }

    public VendorLinkedList getAllVendors() throws IOException {
        return FileHandler.getAllVendors();
    }

    public List<Vendor> getAllVendorsList() throws IOException {
        return FileHandler.getAllVendors().getAllVendors();
    }

    public Vendor getVendorById(String id) throws IOException {
        return FileHandler.findVendorById(id);
    }

    public boolean updateVendor(String id, String name, String imageUrl, String description, String category, String contactInfo) throws IOException {
        Vendor vendor = new Vendor(name, imageUrl, description, category, contactInfo);
        return FileHandler.updateVendor(id, vendor);
    }

    public boolean deleteVendor(String id) throws IOException {
        return FileHandler.deleteVendor(id);
    }
}
