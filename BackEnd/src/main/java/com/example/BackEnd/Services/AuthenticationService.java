package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.DataObject.AuthenticationResponse;
import com.example.BackEnd.DataObject.LoginRequest;
import com.example.BackEnd.DataObject.RegisterRequest;
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
        Customer customer = new Customer(request.getEmail(), passwordEncoder.encode(request.getPassword()), false
                , false, request.getFirstName(), request.getLastName());

        try {
            Optional<Customer> customerCheck = customerRepository.findByEmail(request.getEmail());
            Optional<Admin> adminCheck = adminRepository.findByEmail(request.getEmail());
            if (adminCheck.isPresent() || (customerCheck.isPresent() && customerCheck.get().getIsVerified())) {
                return AuthenticationResponse.builder().token("Already Exist").build();
            } else {
                customerRepository.save(customer);
                var jwtToken = jwtService.generateToken(customer);
                //verification
                return AuthenticationResponse.builder()
                        .token("SUCCESS " + jwtToken)
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AuthenticationResponse.builder().token(e.getMessage()).build();
        }

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
        if (customer.isPresent() && customer.get().getIsVerified()) {
            var jwtToken = jwtService.generateToken(customer.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else if (admin.isPresent() && admin.get().getIsVerified()) {
            var jwtToken = jwtService.generateToken(admin.get());
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new NoSuchElementException();
        }
    }
}