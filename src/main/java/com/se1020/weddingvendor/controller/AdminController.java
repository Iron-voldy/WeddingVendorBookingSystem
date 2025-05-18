package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.Admin;
import com.se1020.weddingvendor.model.Booking;
import com.se1020.weddingvendor.model.QuoteRequest;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.model.Banner;
import com.se1020.weddingvendor.model.Vendor;
import com.se1020.weddingvendor.service.AdminService;
import com.se1020.weddingvendor.service.BookingService;
import com.se1020.weddingvendor.service.BannerService;
import com.se1020.weddingvendor.service.QuoteRequestService;
import com.se1020.weddingvendor.service.UserService;
import com.se1020.weddingvendor.service.VendorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private QuoteRequestService quoteRequestService;

    @Autowired
    private BannerService bannerService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String username,
                             @RequestParam String password,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        try {
            Admin admin = adminService.authenticateAdmin(username, password);
            if (admin == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/admin/login";
            }
            session.setAttribute("admin", admin);
            return "redirect:/admin/dashboard";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error during login: " + e.getMessage());
            return "redirect:/admin/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            // Get counts for dashboard
            List<User> users = userService.getAllUsers();
            List<Vendor> vendors = vendorService.getAllVendorsList();
            List<Booking> bookings = bookingService.getAllBookings();
            List<QuoteRequest> quoteRequests = quoteRequestService.getAllQuoteRequests();
            List<Banner> banners = bannerService.getAllBanners();

            model.addAttribute("admin", admin);
            model.addAttribute("userCount", users.size());
            model.addAttribute("vendorCount", vendors.size());
            model.addAttribute("bookingCount", bookings.size());
            model.addAttribute("quoteCount", quoteRequests.size());
            model.addAttribute("bannerCount", banners.size());

            // Get recent bookings and quotes
            model.addAttribute("recentBookings", bookings.size() > 5 ? bookings.subList(0, 5) : bookings);
            model.addAttribute("recentQuotes", quoteRequests.size() > 5 ? quoteRequests.subList(0, 5) : quoteRequests);

            return "admin/dashboard";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading dashboard data: " + e.getMessage());
            return "admin/dashboard";
        }
    }

    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            List<User> users = userService.getAllUsers();
            model.addAttribute("admin", admin);
            model.addAttribute("users", users);
            return "admin/users";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading users: " + e.getMessage());
            return "admin/users";
        }
    }

    @GetMapping("/vendors")
    public String manageVendors(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            List<Vendor> vendors = vendorService.getAllVendorsList();
            model.addAttribute("admin", admin);
            model.addAttribute("vendors", vendors);
            return "admin/vendors";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendors: " + e.getMessage());
            return "admin/vendors";
        }
    }

    // Redirect to the banner management controller
    @GetMapping("/banners")
    public String redirectToBannerManagement() {
        return "redirect:/admin/banner-management";
    }

    @GetMapping("/bookings")
    public String manageBookings(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            List<Booking> bookings = bookingService.getAllBookings();
            model.addAttribute("admin", admin);
            model.addAttribute("bookings", bookings);

            // Get vendor names for each booking
            for (Booking booking : bookings) {
                Vendor vendor = vendorService.getVendorById(booking.getVendorId());
                if (vendor != null) {
                    model.addAttribute("vendorName_" + booking.getId(), vendor.getName());
                }

                User user = userService.getUserById(booking.getUserId());
                if (user != null) {
                    model.addAttribute("userName_" + booking.getId(), user.getName());
                }
            }

            return "admin/bookings";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading bookings: " + e.getMessage());
            return "admin/bookings";
        }
    }

    @GetMapping("/quotes")
    public String manageQuotes(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            List<QuoteRequest> quoteRequests = quoteRequestService.getAllQuoteRequests();
            model.addAttribute("admin", admin);
            model.addAttribute("quoteRequests", quoteRequests);
            return "admin/quotes";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading quote requests: " + e.getMessage());
            return "admin/quotes";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }
}
