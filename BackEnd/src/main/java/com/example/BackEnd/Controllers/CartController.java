package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.CartRequest;
import com.example.BackEnd.Services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping("/add")
    ResponseEntity<String> addToCart(@RequestBody CartRequest cartRequest){
        try{
            cartService.addToCart(cartRequest.getToken(),cartRequest.getProductId());
            return ResponseEntity.status(HttpStatus.OK).body("Product is added successfully to the cart");
        }catch(IllegalStateException e){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }catch(IllegalAccessException | UsernameNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/setQuantity")
    ResponseEntity<String> setQuantity(@RequestBody CartRequest cartRequest, @RequestParam int quantity){
        try{
            cartService.setQuantity(cartRequest.getToken(),cartRequest.getProductId(),quantity);
            return ResponseEntity.status(HttpStatus.OK).body("Quantity is set successfully for this product");
        }catch (IllegalStateException e){
            return  ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }



}
