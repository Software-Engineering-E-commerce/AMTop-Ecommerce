package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.ReviewDTO;
import com.example.BackEnd.Services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReviewControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    public void whenPostReview_withValidToken_thenReturnsOk() throws Exception {
        String validToken = "Bearer valid_token";
        ReviewDTO reviewDTO = createReviewDTO();
        String email = "user@example.com";

        when(jwtService.extractUsername("valid_token")).thenReturn(email);
        when(reviewService.addReview(reviewDTO, email)).thenReturn("Review Added Successfully");

        mockMvc.perform(post("/reviews")
                        .header("Authorization", validToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reviewDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review Added Successfully"));
    }

    @Test
    public void whenPostReview_withoutToken_thenReturnsBadRequest() throws Exception {
        ReviewDTO reviewDTO = createReviewDTO();

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reviewDTO)))
                .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
    }

    @Test
    public void whenGetProductReviews_withValidToken_thenReturnsOk() throws Exception {
        String validToken = "Bearer valid_token";
        Long productId = 1L;
        String email = "user@example.com";
        List<ReviewDTO> reviewDTOList = createMockReviewDTOList();

        when(jwtService.extractUsername("valid_token")).thenReturn(email);
        when(reviewService.getReviews(email, productId)).thenReturn(reviewDTOList);

        mockMvc.perform(get("/reviews/{productId}", productId)
                        .header("Authorization", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(reviewDTOList)));
    }

    // Test without token
    @Test
    public void whenGetProductReviews_withoutToken_thenReturnsBadRequest() throws Exception {
        Long productId = 1L;

        mockMvc.perform(get("/reviews/{productId}", productId))
                .andExpect(status().isBadRequest());
    }


    // Test with invalid token format
    @Test
    public void whenGetProductReviews_withInvalidTokenFormat_thenReturnsForbidden() throws Exception {
        String invalidToken = "invalid_token";
        Long productId = 1L;

        mockMvc.perform(get("/reviews/{productId}", productId)
                        .header("Authorization", invalidToken))
                .andExpect(status().isForbidden());
    }

    // Helper method to create a list of ReviewDTO objects
    private List<ReviewDTO> createMockReviewDTOList() {
        ReviewDTO reviewDTO = new ReviewDTO(
                "John Doe", 5.0f, "Great product", LocalDateTime.now(), 1L, false
        );
        return Collections.singletonList(reviewDTO);
    }
    private ReviewDTO createReviewDTO() {
        return new ReviewDTO("John Doe", 5.0f, "Great product",
                LocalDateTime.now(), 1L, false);
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
