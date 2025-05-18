package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.Booking;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.model.Vendor;
import com.se1020.weddingvendor.service.BookingService;
import com.se1020.weddingvendor.service.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private VendorService vendorService;

    @GetMapping
    public String getAllBookings(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
            model.addAttribute("bookings", bookings);
            model.addAttribute("user", user);

            // Get vendor names for each booking
            for (Booking booking : bookings) {
                Vendor vendor = vendorService.getVendorById(booking.getVendorId());
                if (vendor != null) {
                    model.addAttribute("vendorName_" + booking.getId(), vendor.getName());
                }
            }

            return "bookings";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading bookings: " + e.getMessage());
            return "bookings";
        }
    }

    @GetMapping("/create/{vendorId}")
    public String showBookingForm(@PathVariable String vendorId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Vendor vendor = vendorService.getVendorById(vendorId);
            if (vendor == null) {
                return "redirect:/vendors";
            }

            model.addAttribute("vendor", vendor);
            model.addAttribute("user", user);
            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            return "create-booking";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendor: " + e.getMessage());
            return "redirect:/vendors";
        }
    }

    @PostMapping("/create")
    public String createBooking(@RequestParam String vendorId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
                                @RequestParam String eventLocation,
                                @RequestParam String packageSelected,
                                @RequestParam double amount,
                                @RequestParam String specialRequests,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.createBooking(
                    user.getId(), vendorId, eventDate, eventLocation,
                    packageSelected, amount, specialRequests
            );

            redirectAttributes.addFlashAttribute("success", "Booking created successfully! Booking ID: " + booking.getId());
            return "redirect:/bookings";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/create/" + vendorId;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error creating booking: " + e.getMessage());
            return "redirect:/bookings/create/" + vendorId;
        }
    }

    @GetMapping("/details/{id}")
    public String bookingDetails(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied");
                return "redirect:/bookings";
            }

            Vendor vendor = vendorService.getVendorById(booking.getVendorId());

            model.addAttribute("booking", booking);
            model.addAttribute("vendor", vendor);
            model.addAttribute("user", user);
            return "booking-details";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading booking details: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied");
                return "redirect:/bookings";
            }

            // Only allow editing of pending bookings
            if (!"PENDING".equals(booking.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "Only pending bookings can be edited");
                return "redirect:/bookings";
            }

            Vendor vendor = vendorService.getVendorById(booking.getVendorId());

            model.addAttribute("booking", booking);
            model.addAttribute("vendor", vendor);
            model.addAttribute("user", user);
            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            return "edit-booking";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading booking: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBooking(@PathVariable String id,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate eventDate,
                                @RequestParam String eventLocation,
                                @RequestParam String packageSelected,
                                @RequestParam double amount,
                                @RequestParam String specialRequests,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied");
                return "redirect:/bookings";
            }

            // Only allow editing of pending bookings
            if (!"PENDING".equals(booking.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "Only pending bookings can be edited");
                return "redirect:/bookings";
            }

            boolean updated = bookingService.updateBooking(
                    id, booking.getVendorId(), eventDate, eventLocation,
                    packageSelected, amount, specialRequests, booking.getStatus()
            );

            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Booking updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update booking");
            }
            return "redirect:/bookings";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/bookings/edit/" + id;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating booking: " + e.getMessage());
            return "redirect:/bookings/edit/" + id;
        }
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied");
                return "redirect:/bookings";
            }

            // Only allow cancellation of pending or confirmed bookings
            if ("CANCELLED".equals(booking.getStatus())) {
                redirectAttributes.addFlashAttribute("error", "Booking is already cancelled");
                return "redirect:/bookings";
            }

            boolean cancelled = bookingService.cancelBooking(id);
            if (cancelled) {
                redirectAttributes.addFlashAttribute("success", "Booking cancelled successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to cancel booking");
            }
            return "redirect:/bookings";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling booking: " + e.getMessage());
            return "redirect:/bookings";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Booking booking = bookingService.getBookingById(id);
            if (booking == null || !booking.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "Booking not found or access denied");
                return "redirect:/bookings";
            }

            boolean deleted = bookingService.deleteBooking(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Booking deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to delete booking");
            }
            return "redirect:/bookings";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting booking: " + e.getMessage());
            return "redirect:/bookings";
        }
    }
}
