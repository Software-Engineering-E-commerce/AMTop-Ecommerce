package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.ProductResponse;
import com.example.BackEnd.Services.ProductDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productDetails")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ProductDetailsController {
    private final JwtService jwtService;
    private final ProductDetailsService productDetailsService;
    @GetMapping("/viewProduct")
    public ResponseEntity<ProductResponse> viewProduct(HttpServletRequest request, @RequestParam("productID") Long productID){
        // Extract the token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(productDetailsService.getProduct(productID));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
