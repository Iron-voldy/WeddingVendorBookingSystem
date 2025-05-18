package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.Booking;
import com.se1020.weddingvendor.model.Vendor;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private VendorService vendorService;

    public Booking createBooking(String userId, String vendorId, LocalDate eventDate,
                                 String eventLocation, String packageSelected,
                                 double amount, String specialRequests) throws IOException {

        // Validate vendor exists
        Vendor vendor = vendorService.getVendorById(vendorId);
        if (vendor == null) {
            throw new IllegalArgumentException("Vendor not found");
        }

        // Validate event date is in the future
        if (eventDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        Booking booking = new Booking(userId, vendorId, eventDate, eventLocation,
                packageSelected, amount, specialRequests);
        FileHandler.saveBooking(booking);
        return booking;
    }

    public List<Booking> getAllBookings() throws IOException {
        return FileHandler.getAllBookings();
    }

    public List<Booking> getBookingsByUserId(String userId) throws IOException {
        return FileHandler.findBookingsByUserId(userId);
    }

    public List<Booking> getBookingsByVendorId(String vendorId) throws IOException {
        return FileHandler.findBookingsByVendorId(vendorId);
    }

    public Booking getBookingById(String id) throws IOException {
        return FileHandler.findBookingById(id);
    }

    public boolean updateBooking(String id, String vendorId, LocalDate eventDate,
                                 String eventLocation, String packageSelected,
                                 double amount, String specialRequests, String status) throws IOException {

        Booking booking = FileHandler.findBookingById(id);
        if (booking == null) {
            return false;
        }

        // Validate vendor exists
        Vendor vendor = vendorService.getVendorById(vendorId);
        if (vendor == null) {
            throw new IllegalArgumentException("Vendor not found");
        }

        // Validate event date is in the future
        if (eventDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Event date must be in the future");
        }

        booking.setVendorId(vendorId);
        booking.setEventDate(eventDate);
        booking.setEventLocation(eventLocation);
        booking.setPackageSelected(packageSelected);
        booking.setAmount(amount);
        booking.setSpecialRequests(specialRequests);
        booking.setStatus(status);

        return FileHandler.updateBooking(booking);
    }

    public boolean updateBookingStatus(String id, String status) throws IOException {
        Booking booking = FileHandler.findBookingById(id);
        if (booking == null) {
            return false;
        }

        booking.setStatus(status);
        return FileHandler.updateBooking(booking);
    }

    public boolean deleteBooking(String id) throws IOException {
        return FileHandler.deleteBooking(id);
    }

    public boolean cancelBooking(String id) throws IOException {
        Booking booking = FileHandler.findBookingById(id);
        if (booking == null) {
            return false;
        }

        booking.setStatus("CANCELLED");
        return FileHandler.updateBooking(booking);
    }
}
