package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.*;
import com.example.BackEnd.Services.CustomerOrdersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customerOrders")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class CustomerOrdersController {
    private final CustomerOrdersService customerOrdersService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    public Optional<Customer> getOptionalCustomer(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return customerRepository.findByEmail(email);
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @GetMapping("/getOrders")
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization") String authorizationHeader) {
        Optional<Customer> customer = getOptionalCustomer(authorizationHeader);
        if (customer.isPresent()) {
            return ResponseEntity.ok(customerOrdersService.retrieveOrders(customer.get()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }
    }
}