package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.Review;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.service.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String getAllReviews(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Review> allReviews = reviewService.getAllReviews();
            model.addAttribute("reviews", allReviews);
            model.addAttribute("user", user);
            return "reviews";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading reviews: " + e.getMessage());
            return "reviews";
        }
    }

    @GetMapping("/my-reviews")
    public String getMyReviews(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Review> userReviews = reviewService.getReviewsByUserId(user.getId());
            model.addAttribute("reviews", userReviews);
            model.addAttribute("user", user);
            return "my-reviews";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading your reviews: " + e.getMessage());
            return "my-reviews";
        }
    }

    @PostMapping("/create")
    public String createReview(@RequestParam String vendorName,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            reviewService.createReview(user.getId(), vendorName, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Review submitted successfully");
            return "redirect:/reviews/my-reviews";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error creating review: " + e.getMessage());
            return "redirect:/reviews/my-reviews";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Review review = reviewService.getReviewById(id);
            if (review == null || !review.getUserId().equals(user.getId())) {
                return "redirect:/reviews/my-reviews";
            }
            model.addAttribute("review", review);
            return "edit-review";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading review: " + e.getMessage());
            return "redirect:/reviews/my-reviews";
        }
    }

    @PostMapping("/update/{id}")
    public String updateReview(@PathVariable String id,
                               @RequestParam String vendorName,
                               @RequestParam int rating,
                               @RequestParam String comment,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Review review = reviewService.getReviewById(id);
            if (review == null || !review.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only edit your own reviews");
                return "redirect:/reviews/my-reviews";
            }

            reviewService.updateReview(id, vendorName, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Review updated successfully");
            return "redirect:/reviews/my-reviews";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating review: " + e.getMessage());
            return "redirect:/reviews/my-reviews";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteReview(@PathVariable String id,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Review review = reviewService.getReviewById(id);
            if (review == null || !review.getUserId().equals(user.getId())) {
                redirectAttributes.addFlashAttribute("error", "You can only delete your own reviews");
                return "redirect:/reviews/my-reviews";
            }

            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("success", "Review deleted successfully");
            return "redirect:/reviews/my-reviews";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting review: " + e.getMessage());
            return "redirect:/reviews/my-reviews";
        }
    }
}
