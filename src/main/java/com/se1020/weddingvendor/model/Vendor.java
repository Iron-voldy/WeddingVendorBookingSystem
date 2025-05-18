package com.se1020.weddingvendor.model;

import java.io.Serializable;
import java.util.UUID;

public class Vendor implements Serializable {
    private String id;
    private String name;
    private String imageUrl;
    private String description;
    private String category;
    private String contactInfo;

    public Vendor() {
        this.id = UUID.randomUUID().toString();
    }

    public Vendor(String name, String imageUrl, String description, String category, String contactInfo) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.contactInfo = contactInfo;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + imageUrl + "," + description + "," + category + "," + contactInfo;
    }
}
