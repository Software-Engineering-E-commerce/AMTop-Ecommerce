package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.AdminProduct;
import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.AdminCatalogService;
import com.example.BackEnd.Services.CustomerCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class CustomerCatalogController {
    private final CustomerCatalogService customerCatalogService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final AdminCatalogService adminCatalogService;
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @GetMapping("/getCustomerProducts")
    public ResponseEntity<List<CustomerProduct>> getProducts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        String customerEmail = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByEmail(customerEmail);
        if (customer.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(customerCatalogService.getProducts(customer.get().getId()));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }
    }
    @GetMapping("/getAdminProducts")
    public ResponseEntity<List<CustomerProduct>> getAdminProducts(@RequestHeader("Authorization") String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        String adminEmail = jwtService.extractUsername(token);
        Optional<Admin> admin =adminRepository.findByEmail(adminEmail);
        if (admin.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(adminCatalogService.getAdminProducts());
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }
    }
}
