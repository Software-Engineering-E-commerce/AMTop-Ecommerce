package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.DTO.AuthenticationResponse;
import com.example.BackEnd.DTO.LoginRequest;
import com.example.BackEnd.DTO.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse customerRegister(RegisterRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setIsGmail(false);
        customer.setIsVerified(false);
        customerRepository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(LoginRequest request) throws NoSuchElementException {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

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
            throw new NoSuchElementException();
        }
    }
}
