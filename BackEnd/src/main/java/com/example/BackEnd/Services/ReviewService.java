package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.ReviewDTO;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.Review;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final CustomerRepository customerRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public String addReview(ReviewDTO reviewDTO, String email){
        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            if (optionalCustomer.isEmpty()) {
                return "User not found with email: " + email;
            }

            Optional<Product> optionalProduct = productRepository.findById(reviewDTO.getProductId());
            if (optionalProduct.isEmpty()) {
                return "Product not found with ID: " + reviewDTO.getProductId();
            }

            Review review = new Review();
            review.setCustomer(optionalCustomer.get());
            review.setProduct(optionalProduct.get());
            review.setRating(reviewDTO.getRating());
            review.setComment(reviewDTO.getComment());
            review.setDate(reviewDTO.getDate());

            reviewRepository.save(review);

            return "Review Added Successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
    public List<ReviewDTO> getReviews(String email, long productId) {
        boolean hasReviewed = false; // Declare hasReviewed outside the try block

        try {
            Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isEmpty()) {
                return new ArrayList<>(); // Return an empty list if the product is not found
            }
            Product product = optionalProduct.get();
            List<Review> reviews = reviewRepository.findByProduct(product);

            if (optionalCustomer.isPresent()) {
                Customer customer = optionalCustomer.get();
                // Update hasReviewed based on the presence of the customer's review
                hasReviewed = reviews.stream().anyMatch(review -> review.getCustomer().equals(customer));
            }

            // Transform each Review entity into a ReviewDTO
            boolean finalHasReviewed = hasReviewed;
            return reviews.stream().map(review -> new ReviewDTO(
                    review.getCustomer().getFirstName() + " " + review.getCustomer().getLastName(),
                    review.getRating(),
                    review.getComment(),
                    review.getDate(),
                    finalHasReviewed
            )).collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list in case of an error
        }
    }
}