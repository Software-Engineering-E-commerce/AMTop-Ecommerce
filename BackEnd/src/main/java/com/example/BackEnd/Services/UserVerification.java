package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserVerification {

    private final JwtService jwtService;
    private final CustomerRepository CustomerRepository;
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public void verifyUser(String token) {
        String username = jwtService.extractUsername(token);
        Optional<Customer> customer = CustomerRepository.findByEmail(username);
        if (customer.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        Customer user = customer.get();
        if (!user.getIsVerified()) {
            user.setIsVerified(true);
            CustomerRepository.save(user);
        } else {
            throw new IllegalStateException("User already verified");
        }
    }

    public void verifyAdmin(String token, RegisterRequest request) {
        String email = jwtService.extractUsername(token);
        if (!email.equals(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "The provided form mail doesn't match the verification mail sent");
        }
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isEmpty()) {
            throw new UsernameNotFoundException("Admin not found");
        }
        Admin admin = optionalAdmin.get();
        if (!admin.getIsVerified()) {
            admin.setIsVerified(true);
            admin.setPassword(passwordEncoder.encode(request.getPassword()));
            admin.setFirstName(request.getFirstName());
            admin.setLastName(request.getLastName());
            adminRepository.save(admin);
        } else {
            throw new IllegalStateException("Admin already verified");
        }
    }
}
