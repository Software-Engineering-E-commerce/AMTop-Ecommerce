package com.example.BackEnd.Controllers;
import com.example.BackEnd.DTO.WishlistElement;
import com.example.BackEnd.DTO.WishlistRequest;
import com.example.BackEnd.Services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/wishlist")
public class WishlistController {
    private final WishlistService wishlistService;

    // Extract the token given the authorization header 'Bearer ${userToken}'
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@RequestHeader("Authorization") String authorizationHeader
            , @RequestBody WishlistRequest wishlistRequest) {
        try {
            String token = extractToken(authorizationHeader);
            wishlistService.addToWishlist(token, wishlistRequest.getProductId());
            return ResponseEntity.status(HttpStatus.OK).body("Product is added successfully to the wishlist");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromWishlist(@RequestHeader("Authorization") String authorizationHeader
            , @RequestParam("productId") Long productId) {
        try {
            String token = extractToken(authorizationHeader);
            wishlistService.deleteFromWishlist(token, productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product is deleted successfully from the wishlist");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestHeader("Authorization") String authorizationHeader
            , @RequestBody WishlistRequest wishlistRequest){
        try {
            String token = extractToken(authorizationHeader);
            wishlistService.addToCart(token, wishlistRequest.getProductId());
            return ResponseEntity.status(HttpStatus.OK).body("Product added successfully to your cart");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getWishlistElements")
    public ResponseEntity<List<WishlistElement>> getWishlistElements(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String token = extractToken(authorizationHeader);
            List<WishlistElement> wishlistElements = wishlistService.getWishlistElements(token);
            return ResponseEntity.status(HttpStatus.OK).body(wishlistElements);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

}