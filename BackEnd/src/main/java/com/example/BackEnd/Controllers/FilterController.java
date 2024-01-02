package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.FilterService.FilterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/filter")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class FilterController<T extends Comparable<T>> {

    private final FilterService<T> filterService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @PostMapping("/{entity}")
    public List<T> filter(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String entity, @RequestBody Object criteria) {
        String token = extractToken(authorizationHeader);
        String email = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByEmail(email);
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (customer.isPresent())
            return filterService.filter(entity, criteria, customer.get().getId());
        else if (admin.isPresent())
            return filterService.filter(entity, criteria, admin.get().getId());
        else
            return new ArrayList<>();

    }
}
