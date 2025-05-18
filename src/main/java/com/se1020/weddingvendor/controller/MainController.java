package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.Banner;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.service.BannerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private BannerService bannerService;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }

        try {
            List<Banner> activeBanners = bannerService.getActiveBanners();
            model.addAttribute("banners", activeBanners);
        } catch (IOException e) {
            // Log the error but don't show it to the user
            System.err.println("Error loading banners: " + e.getMessage());
        }

        return "index";
    }
}
