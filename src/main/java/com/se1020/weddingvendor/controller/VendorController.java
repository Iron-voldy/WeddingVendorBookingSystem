package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.model.Vendor;
import com.se1020.weddingvendor.service.VendorService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/vendors")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @GetMapping
    public String getAllVendors(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Vendor> vendors = vendorService.getAllVendorsList();
            model.addAttribute("vendors", vendors);
            model.addAttribute("user", user);
            return "vendors";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendors: " + e.getMessage());
            return "vendors";
        }
    }

    @GetMapping("/manage")
    public String manageVendors(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Vendor> vendors = vendorService.getAllVendorsList();
            model.addAttribute("vendors", vendors);
            model.addAttribute("user", user);
            return "manage-vendors";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendors: " + e.getMessage());
            return "manage-vendors";
        }
    }

    @GetMapping("/add")
    public String showAddVendorForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "add-vendor";
    }

    @PostMapping("/add")
    public String addVendor(@RequestParam String name,
                            @RequestParam String imageUrl,
                            @RequestParam String description,
                            @RequestParam String category,
                            @RequestParam String contactInfo,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            vendorService.createVendor(name, imageUrl, description, category, contactInfo);
            redirectAttributes.addFlashAttribute("success", "Vendor added successfully");
            return "redirect:/vendors/manage";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error adding vendor: " + e.getMessage());
            return "redirect:/vendors/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditVendorForm(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Vendor vendor = vendorService.getVendorById(id);
            if (vendor == null) {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
                return "redirect:/vendors/manage";
            }
            model.addAttribute("vendor", vendor);
            model.addAttribute("user", user);
            return "edit-vendor";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendor: " + e.getMessage());
            return "redirect:/vendors/manage";
        }
    }

    @PostMapping("/update/{id}")
    public String updateVendor(@PathVariable String id,
                               @RequestParam String name,
                               @RequestParam String imageUrl,
                               @RequestParam String description,
                               @RequestParam String category,
                               @RequestParam String contactInfo,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            boolean updated = vendorService.updateVendor(id, name, imageUrl, description, category, contactInfo);
            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Vendor updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
            }
            return "redirect:/vendors/manage";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating vendor: " + e.getMessage());
            return "redirect:/vendors/manage";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteVendor(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            boolean deleted = vendorService.deleteVendor(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Vendor deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Vendor not found");
            }
            return "redirect:/vendors/manage";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting vendor: " + e.getMessage());
            return "redirect:/vendors/manage";
        }
    }

    @GetMapping("/details/{id}")
    public String vendorDetails(@PathVariable String id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Vendor vendor = vendorService.getVendorById(id);
            if (vendor == null) {
                return "redirect:/vendors";
            }
            model.addAttribute("vendor", vendor);
            model.addAttribute("user", user);
            return "vendor-details";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading vendor details: " + e.getMessage());
            return "redirect:/vendors";
        }
    }
}
