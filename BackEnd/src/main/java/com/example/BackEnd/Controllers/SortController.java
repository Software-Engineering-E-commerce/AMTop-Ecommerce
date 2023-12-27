package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.AdminCatalogService;
import com.example.BackEnd.Services.CustomerCatalogService;
import com.example.BackEnd.Services.SortServices.SortService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sort")
@CrossOrigin(origins = "http://localhost:3000")
public class SortController<T extends Comparable<T>> {

    private final SortService<T> sortService;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @GetMapping("/{entity}/{sortBy}/{sortOrder}")
    public List<T> sort(@RequestHeader("Authorization") String authorizationHeader, @PathVariable String entity,
                        @PathVariable String sortBy, @PathVariable boolean sortOrder) {
        String token = extractToken(authorizationHeader);
        String customerEmail = jwtService.extractUsername(token);
        Optional<Customer> customer = customerRepository.findByEmail(customerEmail);
        System.out.println("SortController: sort");
        System.out.println("entity: " + entity);
        System.out.println("sortBy: " + sortBy);
        System.out.println("sortOrder: " + sortOrder);
        if (customer.isPresent())
            return sortService.sort(entity, sortBy, sortOrder, customer.get().getId());
        else
            return new ArrayList<>();
    }

}
