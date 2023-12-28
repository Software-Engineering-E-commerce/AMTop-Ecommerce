package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.ReviewDTO;
import com.example.BackEnd.Services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ReviewController {
    private final JwtService jwtService;
    private final ReviewService service;

    @PostMapping("/reviews")
    public ResponseEntity<String> addReviews(@RequestHeader("Authorization") String authorizationHeader,
                                             @RequestBody ReviewDTO reviewDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.addReview(reviewDTO, email));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }

    @GetMapping("/reviews/{productId}")
    public ResponseEntity<List<ReviewDTO>> getProductReviews(@RequestHeader("Authorization") String authorizationHeader,
                                                             @PathVariable Long productId) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.getReviews(email, productId));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

}
