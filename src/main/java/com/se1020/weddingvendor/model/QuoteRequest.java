package com.se1020.weddingvendor.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class QuoteRequest implements Serializable {
    private String id;
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;
    private String address;
    private LocalDate weddingDate;
    private String hotelName;
    private String hotelAddress;
    private String hotelContactNumber;
    private String status; // NEW, CONTACTED, QUOTED, BOOKED, DECLINED
    private LocalDateTime createdAt;

    public QuoteRequest() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.status = "NEW";
    }

    public QuoteRequest(String title, String firstName, String lastName, String email,
                        String contactNumber, String address, LocalDate weddingDate,
                        String hotelName, String hotelAddress, String hotelContactNumber) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.weddingDate = weddingDate;
        this.hotelName = hotelName;
        this.hotelAddress = hotelAddress;
        this.hotelContactNumber = hotelContactNumber;
        this.status = "NEW";
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getWeddingDate() {
        return weddingDate;
    }

    public void setWeddingDate(LocalDate weddingDate) {
        this.weddingDate = weddingDate;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelAddress() {
        return hotelAddress;
    }

    public void setHotelAddress(String hotelAddress) {
        this.hotelAddress = hotelAddress;
    }

    public String getHotelContactNumber() {
        return hotelContactNumber;
    }

    public void setHotelContactNumber(String hotelContactNumber) {
        this.hotelContactNumber = hotelContactNumber;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedWeddingDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        return weddingDate.format(formatter);
    }

    public String getFormattedCreatedAt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createdAt.format(formatter);
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return id + "," + title + "," + firstName + "," + lastName + "," +
                email + "," + contactNumber + "," + address + "," +
                weddingDate.format(dateFormatter) + "," + hotelName + "," +
                hotelAddress + "," + hotelContactNumber + "," +
                status + "," + createdAt.format(dateTimeFormatter);
    }
}
