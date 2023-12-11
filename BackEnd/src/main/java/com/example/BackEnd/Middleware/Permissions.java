package com.example.BackEnd.Middleware;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class Permissions {

    private final JwtService jwtService;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;

    public boolean checkAdmin(String token) {
        return adminRepository.findByEmail(jwtService.extractUsername(token)).isPresent();
    }

    public boolean checkCustomer(String token) {
        return customerRepository.findByEmail(jwtService.extractUsername(token)).isPresent();
    }

    public boolean checkAdminOrCustomer(String token) {
        return checkAdmin(token) || checkCustomer(token);
    }

    public boolean checkToken(String authorizationHeader) {
        return authorizationHeader != null &&
                authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7;
    }
}
