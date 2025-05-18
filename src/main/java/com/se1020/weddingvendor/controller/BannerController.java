package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.Admin;
import com.se1020.weddingvendor.model.Banner;
import com.se1020.weddingvendor.service.BannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/banner-management") // Changed from "/admin/banners" to avoid conflict
public class BannerController {

    @Autowired
    private BannerService bannerService;

    @GetMapping
    public String manageBanners(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            List<Banner> banners = bannerService.getAllBanners();
            model.addAttribute("admin", admin);
            model.addAttribute("banners", banners);
            return "admin/banners";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading banners: " + e.getMessage());
            return "admin/banners";
        }
    }

    @GetMapping("/add")
    public String showAddBannerForm(HttpSession session, Model model) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", admin);
        return "admin/add-banner";
    }

    @PostMapping("/add")
    public String addBanner(@RequestParam String title,
                            @RequestParam String description,
                            @RequestParam String imageUrl,
                            @RequestParam String linkUrl,
                            @RequestParam(defaultValue = "true") boolean active,
                            @RequestParam(defaultValue = "0") int displayOrder,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            bannerService.createBanner(title, description, imageUrl, linkUrl, active, displayOrder);
            redirectAttributes.addFlashAttribute("success", "Banner added successfully");
            return "redirect:/admin/banner-management";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error adding banner: " + e.getMessage());
            return "redirect:/admin/banner-management/add";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBannerForm(@PathVariable String id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            Banner banner = bannerService.getBannerById(id);
            if (banner == null) {
                redirectAttributes.addFlashAttribute("error", "Banner not found");
                return "redirect:/admin/banner-management";
            }

            model.addAttribute("admin", admin);
            model.addAttribute("banner", banner);
            return "admin/edit-banner";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading banner: " + e.getMessage());
            return "redirect:/admin/banner-management";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBanner(@PathVariable String id,
                               @RequestParam String title,
                               @RequestParam String description,
                               @RequestParam String imageUrl,
                               @RequestParam String linkUrl,
                               @RequestParam(defaultValue = "false") boolean active,
                               @RequestParam int displayOrder,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            boolean updated = bannerService.updateBanner(id, title, description, imageUrl, linkUrl, active, displayOrder);
            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Banner updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Banner not found");
            }
            return "redirect:/admin/banner-management";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating banner: " + e.getMessage());
            return "redirect:/admin/banner-management/edit/" + id;
        }
    }

    @GetMapping("/toggle/{id}")
    public String toggleBannerStatus(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            boolean toggled = bannerService.toggleBannerStatus(id);
            if (toggled) {
                redirectAttributes.addFlashAttribute("success", "Banner status toggled successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Banner not found");
            }
            return "redirect:/admin/banner-management";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error toggling banner status: " + e.getMessage());
            return "redirect:/admin/banner-management";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBanner(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (admin == null) {
            return "redirect:/admin/login";
        }

        try {
            boolean deleted = bannerService.deleteBanner(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Banner deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Banner not found");
            }
            return "redirect:/admin/banner-management";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting banner: " + e.getMessage());
            return "redirect:/admin/banner-management";
        }
    }
}
