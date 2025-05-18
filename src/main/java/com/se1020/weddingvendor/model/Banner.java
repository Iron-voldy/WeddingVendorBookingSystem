package com.se1020.weddingvendor.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Banner implements Serializable {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String linkUrl;
    private boolean active;
    private int displayOrder;
    private LocalDateTime createdAt;

    public Banner() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.active = true;
        this.displayOrder = 0;
    }

    public Banner(String title, String description, String imageUrl, String linkUrl, boolean active, int displayOrder) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
        this.active = active;
        this.displayOrder = displayOrder;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createdAt.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return id + "," + title + "," + description + "," + imageUrl + "," +
                linkUrl + "," + active + "," + displayOrder + "," +
                createdAt.format(formatter);
    }
}
