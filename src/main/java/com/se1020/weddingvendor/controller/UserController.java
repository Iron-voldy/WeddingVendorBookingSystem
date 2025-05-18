package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import com.se1020.weddingvendor.util.FileHandler;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    // Remove or rename this method to avoid conflict with MainController
    // @GetMapping("/")
    // public String home() {
    //     return "index";
    // }

    // You can keep a welcome page if needed, but with a different URL
    @GetMapping("/welcome")
    public String welcome() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String email,
                               @RequestParam String name,
                               RedirectAttributes redirectAttributes) {
        try {
            User user = userService.registerUser(username, password, email, name);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Username already exists");
                return "redirect:/register";
            }
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error during registration: " + e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.authenticateUser(username, password);
            if (user == null) {
                redirectAttributes.addFlashAttribute("error", "Invalid username or password");
                return "redirect:/login";
            }
            session.setAttribute("user", user);
            return "redirect:/dashboard";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error during login: " + e.getMessage());
            return "redirect:/login";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        try {
            User currentUser = (User) session.getAttribute("user");
            if (currentUser == null) {
                return "redirect:/login";
            }

            // Update user information
            currentUser.setName(name);
            currentUser.setEmail(email);

            // Update in file storage
            List<User> users = FileHandler.getAllUsers();
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId().equals(currentUser.getId())) {
                    users.set(i, currentUser);
                    break;
                }
            }
            FileHandler.saveAllUsers(users);

            // Update in session
            session.setAttribute("user", currentUser);

            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            return "redirect:/profile";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating profile: " + e.getMessage());
            return "redirect:/profile";
        }
    }

}
