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
    private final ProductDetailsService productDetailsService;
    @GetMapping("/viewProduct")
    public ResponseEntity<ProductResponse> viewProduct(HttpServletRequest request, @RequestParam("productID") Long productID){
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return ResponseEntity.ok(productDetailsService.getProduct(productID, token));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
