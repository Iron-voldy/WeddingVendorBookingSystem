package com.se1020.weddingvendor.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Booking implements Serializable {
    private String id;
    private String userId;
    private String vendorId;
    private LocalDate eventDate;
    private String eventLocation;
    private String packageSelected;
    private double amount;
    private String specialRequests;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private LocalDateTime createdAt;

    public Booking() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = "PENDING";
    }

    public Booking(String userId, String vendorId, LocalDate eventDate, String eventLocation,
                   String packageSelected, double amount, String specialRequests) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.vendorId = vendorId;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.packageSelected = packageSelected;
        this.amount = amount;
        this.specialRequests = specialRequests;
        this.status = "PENDING";
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getPackageSelected() {
        return packageSelected;
    }

    public void setPackageSelected(String packageSelected) {
        this.packageSelected = packageSelected;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFormattedEventDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return eventDate.format(formatter);
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createdAt.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return id + "," + userId + "," + vendorId + "," +
                eventDate.format(dateFormatter) + "," + eventLocation + "," +
                packageSelected + "," + amount + "," + specialRequests + "," +
                status + "," + createdAt.format(dateTimeFormatter);
    }
}
