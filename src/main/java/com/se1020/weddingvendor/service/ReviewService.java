package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.Review;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReviewService {

    public Review createReview(String userId, String vendorName, int rating, String comment) throws IOException {
        Review review = new Review(userId, vendorName, rating, comment);
        FileHandler.saveReview(review);
        return review;
    }

    public List<Review> getAllReviews() throws IOException {
        return FileHandler.getAllReviews();
    }

    public List<Review> getReviewsByUserId(String userId) throws IOException {
        return FileHandler.findReviewsByUserId(userId);
    }

    public Review getReviewById(String id) throws IOException {
        return FileHandler.findReviewById(id);
    }

    public Review updateReview(String id, String vendorName, int rating, String comment) throws IOException {
        Review review = FileHandler.findReviewById(id);
        if (review != null) {
            review.setVendorName(vendorName);
            review.setRating(rating);
            review.setComment(comment);
            FileHandler.updateReview(review);
            return review;
        }
        return null;
    }

    public boolean deleteReview(String id) throws IOException {
        Review review = FileHandler.findReviewById(id);
        if (review != null) {
            FileHandler.deleteReview(id);
            return true;
        }
        return false;
    }
}
