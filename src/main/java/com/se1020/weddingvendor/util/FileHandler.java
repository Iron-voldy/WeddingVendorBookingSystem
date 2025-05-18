package com.se1020.weddingvendor.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.se1020.weddingvendor.model.Admin;
import com.se1020.weddingvendor.model.Banner;
import com.se1020.weddingvendor.model.Booking;
import com.se1020.weddingvendor.model.QuoteRequest;
import com.se1020.weddingvendor.model.Review;
import com.se1020.weddingvendor.model.User;
import com.se1020.weddingvendor.model.Vendor;

public class FileHandler {
    private static final String DATA_DIR = "data";
    private static final String USERS_FILE = DATA_DIR + "/users.txt";
    private static final String REVIEWS_FILE = DATA_DIR + "/reviews.txt";
    private static final String VENDORS_FILE = DATA_DIR + "/vendors.txt";
    private static final String BOOKINGS_FILE = DATA_DIR + "/bookings.txt";
    private static final String QUOTE_REQUESTS_FILE = DATA_DIR + "/quote_requests.txt";
    private static final String ADMINS_FILE = DATA_DIR + "/admins.txt";
    private static final String BANNERS_FILE = DATA_DIR + "/banners.txt";

    // Initialize data directory and files
    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            if (!Files.exists(Paths.get(USERS_FILE))) {
                Files.createFile(Paths.get(USERS_FILE));
            }
            if (!Files.exists(Paths.get(REVIEWS_FILE))) {
                Files.createFile(Paths.get(REVIEWS_FILE));
            }
            if (!Files.exists(Paths.get(VENDORS_FILE))) {
                Files.createFile(Paths.get(VENDORS_FILE));
            }
            if (!Files.exists(Paths.get(BOOKINGS_FILE))) {
                Files.createFile(Paths.get(BOOKINGS_FILE));
            }
            if (!Files.exists(Paths.get(QUOTE_REQUESTS_FILE))) {
                Files.createFile(Paths.get(QUOTE_REQUESTS_FILE));
            }
            if (!Files.exists(Paths.get(ADMINS_FILE))) {
                Files.createFile(Paths.get(ADMINS_FILE));
                // Create default admin account
                Admin defaultAdmin = new Admin("admin", "admin123", "admin@weddingvendor.com", "Administrator");
                List<String> adminLine = new ArrayList<>();
                adminLine.add(defaultAdmin.toString());
                Files.write(Paths.get(ADMINS_FILE), adminLine);
            }
            if (!Files.exists(Paths.get(BANNERS_FILE))) {
                Files.createFile(Paths.get(BANNERS_FILE));
                // Create a default banner
                Banner defaultBanner = new Banner(
                        "Welcome to Wedding Vendor Booking",
                        "Find and book the best vendors for your special day",
                        "https://images.unsplash.com/photo-1519741497674-611481863552?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80",
                        "/vendors",
                        true,
                        1
                );
                List<String> bannerLine = new ArrayList<>();
                bannerLine.add(defaultBanner.toString());
                Files.write(Paths.get(BANNERS_FILE), bannerLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // User operations
    public static void saveUser(User user) throws IOException {
        List<User> users = getAllUsers();
        users.add(user);
        saveAllUsers(users);
    }

    public static List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(USERS_FILE));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                User user = new User();
                user.setId(parts[0]);
                user.setUsername(parts[1]);
                user.setPassword(parts[2]);
                user.setEmail(parts[3]);
                user.setName(parts[4]);
                users.add(user);
            }
        }
        return users;
    }

    public static void saveAllUsers(List<User> users) throws IOException {
        List<String> lines = users.stream()
                .map(User::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(USERS_FILE), lines);
    }

    public static User findUserByUsername(String username) throws IOException {
        return getAllUsers().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static User findUserById(String id) throws IOException {
        return getAllUsers().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Review operations
    public static void saveReview(Review review) throws IOException {
        List<Review> reviews = getAllReviews();
        reviews.add(review);
        saveAllReviews(reviews);
    }

    public static List<Review> getAllReviews() throws IOException {
        List<Review> reviews = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(REVIEWS_FILE));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                Review review = new Review();
                review.setId(parts[0]);
                review.setUserId(parts[1]);
                review.setVendorName(parts[2]);
                review.setRating(Integer.parseInt(parts[3]));
                review.setComment(parts[4]);
                review.setCreatedAt(LocalDateTime.parse(parts[5], formatter));
                reviews.add(review);
            }
        }
        return reviews;
    }

    public static void saveAllReviews(List<Review> reviews) throws IOException {
        List<String> lines = reviews.stream()
                .map(Review::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(REVIEWS_FILE), lines);
    }

    public static Review findReviewById(String id) throws IOException {
        return getAllReviews().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Review> findReviewsByUserId(String userId) throws IOException {
        return getAllReviews().stream()
                .filter(r -> r.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static void updateReview(Review updatedReview) throws IOException {
        List<Review> reviews = getAllReviews();
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getId().equals(updatedReview.getId())) {
                reviews.set(i, updatedReview);
                break;
            }
        }
        saveAllReviews(reviews);
    }

    public static void deleteReview(String id) throws IOException {
        List<Review> reviews = getAllReviews();
        reviews.removeIf(r -> r.getId().equals(id));
        saveAllReviews(reviews);
    }

    // Vendor operations
    public static VendorLinkedList getAllVendors() throws IOException {
        VendorLinkedList vendorList = new VendorLinkedList();
        List<String> lines = Files.readAllLines(Paths.get(VENDORS_FILE));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 6) {
                Vendor vendor = new Vendor();
                vendor.setId(parts[0]);
                vendor.setName(parts[1]);
                vendor.setImageUrl(parts[2]);
                vendor.setDescription(parts[3]);
                vendor.setCategory(parts[4]);
                vendor.setContactInfo(parts[5]);
                vendorList.add(vendor);
            }
        }
        return vendorList;
    }

    public static void saveAllVendors(VendorLinkedList vendorList) throws IOException {
        List<String> lines = new ArrayList<>();
        List<Vendor> vendors = vendorList.getAllVendors();

        for (Vendor vendor : vendors) {
            lines.add(vendor.toString());
        }

        Files.write(Paths.get(VENDORS_FILE), lines);
    }

    public static void saveVendor(Vendor vendor) throws IOException {
        VendorLinkedList vendorList = getAllVendors();
        vendorList.add(vendor);
        saveAllVendors(vendorList);
    }

    public static Vendor findVendorById(String id) throws IOException {
        VendorLinkedList vendorList = getAllVendors();
        return vendorList.findById(id);
    }

    public static boolean updateVendor(String id, Vendor updatedVendor) throws IOException {
        VendorLinkedList vendorList = getAllVendors();
        boolean updated = vendorList.update(id, updatedVendor);
        if (updated) {
            saveAllVendors(vendorList);
        }
        return updated;
    }

    public static boolean deleteVendor(String id) throws IOException {
        VendorLinkedList vendorList = getAllVendors();
        boolean deleted = vendorList.delete(id);
        if (deleted) {
            saveAllVendors(vendorList);
        }
        return deleted;
    }

    // Booking operations
    public static void saveBooking(Booking booking) throws IOException {
        List<Booking> bookings = getAllBookings();
        bookings.add(booking);
        saveAllBookings(bookings);
    }

    public static List<Booking> getAllBookings() throws IOException {
        List<Booking> bookings = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(BOOKINGS_FILE));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 10) {
                Booking booking = new Booking();
                booking.setId(parts[0]);
                booking.setUserId(parts[1]);
                booking.setVendorId(parts[2]);
                booking.setEventDate(LocalDate.parse(parts[3], dateFormatter));
                booking.setEventLocation(parts[4]);
                booking.setPackageSelected(parts[5]);
                booking.setAmount(Double.parseDouble(parts[6]));
                booking.setSpecialRequests(parts[7]);
                booking.setStatus(parts[8]);
                booking.setCreatedAt(LocalDateTime.parse(parts[9], dateTimeFormatter));
                bookings.add(booking);
            }
        }
        return bookings;
    }

    public static void saveAllBookings(List<Booking> bookings) throws IOException {
        List<String> lines = bookings.stream()
                .map(Booking::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(BOOKINGS_FILE), lines);
    }

    public static Booking findBookingById(String id) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Booking> findBookingsByUserId(String userId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public static List<Booking> findBookingsByVendorId(String vendorId) throws IOException {
        return getAllBookings().stream()
                .filter(b -> b.getVendorId().equals(vendorId))
                .collect(Collectors.toList());
    }

    public static boolean updateBooking(Booking updatedBooking) throws IOException {
        List<Booking> bookings = getAllBookings();
        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getId().equals(updatedBooking.getId())) {
                bookings.set(i, updatedBooking);
                saveAllBookings(bookings);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteBooking(String id) throws IOException {
        List<Booking> bookings = getAllBookings();
        boolean removed = bookings.removeIf(b -> b.getId().equals(id));
        if (removed) {
            saveAllBookings(bookings);
        }
        return removed;
    }

    // Quote Request operations
    public static void saveQuoteRequest(QuoteRequest quoteRequest) throws IOException {
        List<QuoteRequest> quoteRequests = getAllQuoteRequests();
        quoteRequests.add(quoteRequest);
        saveAllQuoteRequests(quoteRequests);
    }

    public static List<QuoteRequest> getAllQuoteRequests() throws IOException {
        List<QuoteRequest> quoteRequests = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(QUOTE_REQUESTS_FILE));

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 13) {
                QuoteRequest quoteRequest = new QuoteRequest();
                quoteRequest.setId(parts[0]);
                quoteRequest.setTitle(parts[1]);
                quoteRequest.setFirstName(parts[2]);
                quoteRequest.setLastName(parts[3]);
                quoteRequest.setEmail(parts[4]);
                quoteRequest.setContactNumber(parts[5]);
                quoteRequest.setAddress(parts[6]);
                quoteRequest.setWeddingDate(LocalDate.parse(parts[7], dateFormatter));
                quoteRequest.setHotelName(parts[8]);
                quoteRequest.setHotelAddress(parts[9]);
                quoteRequest.setHotelContactNumber(parts[10]);
                quoteRequest.setStatus(parts[11]);
                quoteRequest.setCreatedAt(LocalDateTime.parse(parts[12], dateTimeFormatter));
                quoteRequests.add(quoteRequest);
            }
        }
        return quoteRequests;
    }

    public static void saveAllQuoteRequests(List<QuoteRequest> quoteRequests) throws IOException {
        List<String> lines = quoteRequests.stream()
                .map(QuoteRequest::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(QUOTE_REQUESTS_FILE), lines);
    }

    public static QuoteRequest findQuoteRequestById(String id) throws IOException {
        return getAllQuoteRequests().stream()
                .filter(q -> q.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateQuoteRequest(QuoteRequest updatedQuoteRequest) throws IOException {
        List<QuoteRequest> quoteRequests = getAllQuoteRequests();
        for (int i = 0; i < quoteRequests.size(); i++) {
            if (quoteRequests.get(i).getId().equals(updatedQuoteRequest.getId())) {
                quoteRequests.set(i, updatedQuoteRequest);
                saveAllQuoteRequests(quoteRequests);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteQuoteRequest(String id) throws IOException {
        List<QuoteRequest> quoteRequests = getAllQuoteRequests();
        boolean removed = quoteRequests.removeIf(q -> q.getId().equals(id));
        if (removed) {
            saveAllQuoteRequests(quoteRequests);
        }
        return removed;
    }

    // Admin operations
    public static List<Admin> getAllAdmins() throws IOException {
        List<Admin> admins = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(ADMINS_FILE));

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                Admin admin = new Admin();
                admin.setId(parts[0]);
                admin.setUsername(parts[1]);
                admin.setPassword(parts[2]);
                admin.setEmail(parts[3]);
                admin.setName(parts[4]);
                admins.add(admin);
            }
        }
        return admins;
    }

    public static void saveAllAdmins(List<Admin> admins) throws IOException {
        List<String> lines = admins.stream()
                .map(Admin::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(ADMINS_FILE), lines);
    }

    public static Admin findAdminByUsername(String username) throws IOException {
        return getAllAdmins().stream()
                .filter(a -> a.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static Admin findAdminById(String id) throws IOException {
        return getAllAdmins().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Banner operations
    public static void saveBanner(Banner banner) throws IOException {
        List<Banner> banners = getAllBanners();
        banners.add(banner);
        saveAllBanners(banners);
    }

    public static List<Banner> getAllBanners() throws IOException {
        List<Banner> banners = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(BANNERS_FILE));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length >= 8) {
                Banner banner = new Banner();
                banner.setId(parts[0]);
                banner.setTitle(parts[1]);
                banner.setDescription(parts[2]);
                banner.setImageUrl(parts[3]);
                banner.setLinkUrl(parts[4]);
                banner.setActive(Boolean.parseBoolean(parts[5]));
                banner.setDisplayOrder(Integer.parseInt(parts[6]));
                banner.setCreatedAt(LocalDateTime.parse(parts[7], formatter));
                banners.add(banner);
            }
        }

        // Sort banners by display order
        banners.sort((b1, b2) -> Integer.compare(b1.getDisplayOrder(), b2.getDisplayOrder()));
        return banners;
    }

    public static List<Banner> getActiveBanners() throws IOException {
        return getAllBanners().stream()
                .filter(Banner::isActive)
                .collect(Collectors.toList());
    }

    public static void saveAllBanners(List<Banner> banners) throws IOException {
        List<String> lines = banners.stream()
                .map(Banner::toString)
                .collect(Collectors.toList());
        Files.write(Paths.get(BANNERS_FILE), lines);
    }

    public static Banner findBannerById(String id) throws IOException {
        return getAllBanners().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static boolean updateBanner(Banner updatedBanner) throws IOException {
        List<Banner> banners = getAllBanners();
        for (int i = 0; i < banners.size(); i++) {
            if (banners.get(i).getId().equals(updatedBanner.getId())) {
                banners.set(i, updatedBanner);
                saveAllBanners(banners);
                return true;
            }
        }
        return false;
    }

    public static boolean deleteBanner(String id) throws IOException {
        List<Banner> banners = getAllBanners();
        boolean removed = banners.removeIf(b -> b.getId().equals(id));
        if (removed) {
            saveAllBanners(banners);
        }
        return removed;
    }
}
