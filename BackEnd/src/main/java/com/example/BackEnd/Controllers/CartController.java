package com.example.BackEnd.Controllers;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.DTO.CartRequest;
import com.example.BackEnd.Services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CartRequest cartRequest) {
        String token = extractToken(authorizationHeader);
        try {
            cartService.addToCart(token, cartRequest.getProductId());
            return ResponseEntity.status(HttpStatus.OK).body("Product is added successfully to the cart");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/setQuantity")
    public ResponseEntity<String> setQuantity(@RequestHeader("Authorization") String authorizationHeader, @RequestBody CartRequest cartRequest, @RequestParam("quantity") int quantity) {
        String token = extractToken(authorizationHeader);
        if (quantity == 0) {
            return deleteFromCart(authorizationHeader, cartRequest.getProductId());
        }
        try {
            cartService.setQuantity(token, cartRequest.getProductId(), quantity);
            return ResponseEntity.status(HttpStatus.OK).body("Quantity is set successfully for this product");
        } catch (IllegalStateException e) {
            //the status is OK because we want the message in e.getMessage() to show up there
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFromCart(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("productId") Long productId) {
        String token = extractToken(authorizationHeader);
        try {
            cartService.deleteFromCart(token, productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product is deleted successfully from the cart");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping("/getCartElements")
    public ResponseEntity<List<CartElement>> getCartElements(@RequestHeader("Authorization") String authorizationHeader){
        String token = extractToken(authorizationHeader);
        List<CartElement> cartElements = cartService.getCartElements(token);
        return ResponseEntity.status(HttpStatus.OK).body(cartElements);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestHeader("Authorization") String authorizationHeader){
        String token = extractToken(authorizationHeader);
        try{
            cartService.checkout(token);
            return ResponseEntity.status(HttpStatus.OK).body("Order has been placed successfully !");
        } catch (IllegalStateException exception){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
        }
    }
}
