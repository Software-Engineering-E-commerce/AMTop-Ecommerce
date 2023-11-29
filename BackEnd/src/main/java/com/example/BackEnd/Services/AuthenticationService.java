package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.DTO.AuthenticationResponse;
import com.example.BackEnd.DTO.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(LoginRequest request) throws NoSuchElementException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception e) {
            return AuthenticationResponse.builder().token("Unauthorized").build();
        }

        Optional<Customer> customer = customerRepository.findByEmail(request.getEmail());
        Optional<Admin> admin = adminRepository.findByEmail(request.getEmail());
        if (customer.isPresent() && customer.get().getIsVerified() && !customer.get().getIsGmail()) {
            var jwtToken = jwtService.generateToken(customer.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else if (admin.isPresent() && admin.get().getIsVerified() && !admin.get().getIsGmail()) {
            var jwtToken = jwtService.generateToken(admin.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            return AuthenticationResponse.builder()
                    .token("Unauthorized")
                    .build();
        }
    }
}
