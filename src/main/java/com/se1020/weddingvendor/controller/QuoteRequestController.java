package com.se1020.weddingvendor.controller;

import com.se1020.weddingvendor.model.QuoteRequest;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.service.QuoteRequestService;

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
@RequestMapping("/quotes")
public class QuoteRequestController {

    @Autowired
    private QuoteRequestService quoteRequestService;

    @GetMapping
    public String getAllQuoteRequests(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<QuoteRequest> quoteRequests = quoteRequestService.getAllQuoteRequests();
            model.addAttribute("quoteRequests", quoteRequests);
            model.addAttribute("user", user);
            return "quote-requests";
        } catch (IOException e) {
            model.addAttribute("error", "Error loading quote requests: " + e.getMessage());
            return "quote-requests";
        }
    }

    @GetMapping("/create")
    public String showQuoteRequestForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        // Allow non-logged in users to request quotes
        if (user != null) {
            model.addAttribute("user", user);
        }

        model.addAttribute("minDate", LocalDate.now().plusDays(1));
        return "create-quote-request";
    }

    @PostMapping("/create")
    public String createQuoteRequest(@RequestParam String title,
                                     @RequestParam String firstName,
                                     @RequestParam String lastName,
                                     @RequestParam String email,
                                     @RequestParam String contactNumber,
                                     @RequestParam String address,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weddingDate,
                                     @RequestParam String hotelName,
                                     @RequestParam String hotelAddress,
                                     @RequestParam String hotelContactNumber,
                                     RedirectAttributes redirectAttributes) {
        try {
            QuoteRequest quoteRequest = quoteRequestService.createQuoteRequest(
                    title, firstName, lastName, email, contactNumber, address,
                    weddingDate, hotelName, hotelAddress, hotelContactNumber
            );

            redirectAttributes.addFlashAttribute("success", "Your quote request has been submitted successfully! We will contact you soon.");
            return "redirect:/quotes/thank-you";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/quotes/create";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting quote request: " + e.getMessage());
            return "redirect:/quotes/create";
        }
    }

    @GetMapping("/thank-you")
    public String thankYou(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "quote-thank-you";
    }

    @GetMapping("/details/{id}")
    public String quoteRequestDetails(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            QuoteRequest quoteRequest = quoteRequestService.getQuoteRequestById(id);
            if (quoteRequest == null) {
                redirectAttributes.addFlashAttribute("error", "Quote request not found");
                return "redirect:/quotes";
            }

            model.addAttribute("quoteRequest", quoteRequest);
            model.addAttribute("user", user);
            return "quote-request-details";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading quote request details: " + e.getMessage());
            return "redirect:/quotes";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            QuoteRequest quoteRequest = quoteRequestService.getQuoteRequestById(id);
            if (quoteRequest == null) {
                redirectAttributes.addFlashAttribute("error", "Quote request not found");
                return "redirect:/quotes";
            }

            model.addAttribute("quoteRequest", quoteRequest);
            model.addAttribute("user", user);
            model.addAttribute("minDate", LocalDate.now().plusDays(1));
            return "edit-quote-request";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading quote request: " + e.getMessage());
            return "redirect:/quotes";
        }
    }

    @PostMapping("/update/{id}")
    public String updateQuoteRequest(@PathVariable String id,
                                     @RequestParam String title,
                                     @RequestParam String firstName,
                                     @RequestParam String lastName,
                                     @RequestParam String email,
                                     @RequestParam String contactNumber,
                                     @RequestParam String address,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weddingDate,
                                     @RequestParam String hotelName,
                                     @RequestParam String hotelAddress,
                                     @RequestParam String hotelContactNumber,
                                     @RequestParam String status,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            boolean updated = quoteRequestService.updateQuoteRequest(
                    id, title, firstName, lastName, email, contactNumber, address,
                    weddingDate, hotelName, hotelAddress, hotelContactNumber, status
            );

            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Quote request updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update quote request");
            }
            return "redirect:/quotes";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/quotes/edit/" + id;
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating quote request: " + e.getMessage());
            return "redirect:/quotes/edit/" + id;
        }
    }

    @GetMapping("/update-status/{id}")
    public String showUpdateStatusForm(@PathVariable String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            QuoteRequest quoteRequest = quoteRequestService.getQuoteRequestById(id);
            if (quoteRequest == null) {
                redirectAttributes.addFlashAttribute("error", "Quote request not found");
                return "redirect:/quotes";
            }

            model.addAttribute("quoteRequest", quoteRequest);
            model.addAttribute("user", user);
            return "update-quote-status";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error loading quote request: " + e.getMessage());
            return "redirect:/quotes";
        }
    }

    @PostMapping("/update-status/{id}")
    public String updateQuoteRequestStatus(@PathVariable String id,
                                           @RequestParam String status,
                                           HttpSession session,
                                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            boolean updated = quoteRequestService.updateQuoteRequestStatus(id, status);
            if (updated) {
                redirectAttributes.addFlashAttribute("success", "Quote request status updated successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to update quote request status");
            }
            return "redirect:/quotes";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error updating quote request status: " + e.getMessage());
            return "redirect:/quotes";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteQuoteRequest(@PathVariable String id, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            boolean deleted = quoteRequestService.deleteQuoteRequest(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Quote request deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to delete quote request");
            }
            return "redirect:/quotes";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting quote request: " + e.getMessage());
            return "redirect:/quotes";
        }
    }
}
