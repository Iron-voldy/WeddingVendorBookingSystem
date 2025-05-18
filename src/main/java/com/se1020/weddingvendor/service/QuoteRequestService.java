package com.se1020.weddingvendor.service;

import com.se1020.weddingvendor.model.QuoteRequest;
import com.se1020.weddingvendor.util.FileHandler;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class QuoteRequestService {

    public QuoteRequest createQuoteRequest(String title, String firstName, String lastName,
                                           String email, String contactNumber, String address,
                                           LocalDate weddingDate, String hotelName,
                                           String hotelAddress, String hotelContactNumber) throws IOException {

        // Validate wedding date is in the future
        if (weddingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Wedding date must be in the future");
        }

        QuoteRequest quoteRequest = new QuoteRequest(title, firstName, lastName, email,
                contactNumber, address, weddingDate,
                hotelName, hotelAddress, hotelContactNumber);
        FileHandler.saveQuoteRequest(quoteRequest);
        return quoteRequest;
    }

    public List<QuoteRequest> getAllQuoteRequests() throws IOException {
        return FileHandler.getAllQuoteRequests();
    }

    public QuoteRequest getQuoteRequestById(String id) throws IOException {
        return FileHandler.findQuoteRequestById(id);
    }

    public boolean updateQuoteRequest(String id, String title, String firstName, String lastName,
                                      String email, String contactNumber, String address,
                                      LocalDate weddingDate, String hotelName,
                                      String hotelAddress, String hotelContactNumber,
                                      String status) throws IOException {

        QuoteRequest quoteRequest = FileHandler.findQuoteRequestById(id);
        if (quoteRequest == null) {
            return false;
        }

        // Validate wedding date is in the future
        if (weddingDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Wedding date must be in the future");
        }

        quoteRequest.setTitle(title);
        quoteRequest.setFirstName(firstName);
        quoteRequest.setLastName(lastName);
        quoteRequest.setEmail(email);
        quoteRequest.setContactNumber(contactNumber);
        quoteRequest.setAddress(address);
        quoteRequest.setWeddingDate(weddingDate);
        quoteRequest.setHotelName(hotelName);
        quoteRequest.setHotelAddress(hotelAddress);
        quoteRequest.setHotelContactNumber(hotelContactNumber);
        quoteRequest.setStatus(status);

        return FileHandler.updateQuoteRequest(quoteRequest);
    }

    public boolean updateQuoteRequestStatus(String id, String status) throws IOException {
        QuoteRequest quoteRequest = FileHandler.findQuoteRequestById(id);
        if (quoteRequest == null) {
            return false;
        }

        quoteRequest.setStatus(status);
        return FileHandler.updateQuoteRequest(quoteRequest);
    }

    public boolean deleteQuoteRequest(String id) throws IOException {
        return FileHandler.deleteQuoteRequest(id);
    }
}
