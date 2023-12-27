package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.ReviewDTO;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.Review;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ReviewService reviewService;
    private ReviewDTO reviewDTO;
    private Customer customer;
    private Product product;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        product = new Product();
        reviewDTO = new ReviewDTO("John Doe", 4.5f, "Great product",
                LocalDateTime.now(), 1L, false);
    }

    @Test
    void whenAddReviewIsSuccessful_thenReturnsSuccessMessage() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        String response = reviewService.addReview(reviewDTO, "customer@example.com");

        assertEquals("Review Added Successfully", response);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void whenCustomerAddsReview_andCustomerNotFound_thenReturnsErrorMessage() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        String response = reviewService.addReview(reviewDTO, "customer@example.com");

        assertEquals("User not found with email: customer@example.com", response);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void whenCustomerAddsReview_andProductNotFound_thenReturnsErrorMessage() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        String response = reviewService.addReview(reviewDTO, "customer@example.com");

        assertEquals("Product not found with ID: " + reviewDTO.getProductId(), response);
        verify(reviewRepository, never()).save(any(Review.class));
    }

    @Test
    void whenCustomerAddsReview_andExceptionOccurs_thenReturnsErrorMessage() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doThrow(new RuntimeException("Database error")).when(reviewRepository).save(any(Review.class));

        String response = reviewService.addReview(reviewDTO, "customer@example.com");

        assertTrue(response.startsWith("An error occurred:"));
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    public void givenProductAndCustomerFoundAndCustomerHasReviewed_whenGetReviews_thenHasReviewedTrue() {
        // Arrange
        String email = "test@example.com";
        long productId = 1L;
        Customer customer = new Customer();
        Product product = new Product();
        Review review = new Review(customer, product, 5.0f, "Great product", LocalDateTime.now());

        Mockito.when(customerRepository.findByEmail(email)).thenReturn(Optional.of(customer));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(reviewRepository.findByProduct(product)).thenReturn(Collections.singletonList(review));

        // Act
        List<ReviewDTO> result = reviewService.getReviews(email, productId);

        // Assert
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).isHasReviewed());
        assertEquals("Great product", result.get(0).getComment());
    }

}
