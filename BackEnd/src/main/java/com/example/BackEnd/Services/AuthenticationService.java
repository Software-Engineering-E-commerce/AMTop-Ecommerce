package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.AuthenticationResponse;
import com.example.BackEnd.DTO.LoginRequest;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.DTO.AuthenticationResponse;
import com.example.BackEnd.DTO.LoginRequest;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.net.http.HttpHeaders;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
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
                EmailService emailService = new EmailService();
                emailService.sendEmail(customer.getEmail(),"Email Verification",
                        "<body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; padding: 20px;\">\n" +
                                "\n" +
                                "    <div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 10px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\">\n" +
                                "\n" +
                                "        <h2 style=\"color: #333333;\">Email Verification</h2>\n" +
                                "\n" +
                                "        <p style=\"color: #666666;\">Please click on the button below to verify your account:</p>\n" +
                                "\n" +
                                "        <a href=\"http://localhost:3000/verification?token=" + jwtToken + "\"style=\"display: inline-block; background-color: #4caf50; color: #ffffff; padding: 10px 20px; text-decoration: none; border-radius: 5px; margin-top: 20px;\">Verify</a>\n" +
                                "\n" +
                                "    </div>\n" +
                                "</body>");

                return AuthenticationResponse.builder()
                        .token("SUCCESS")
                        .build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return AuthenticationResponse.builder().token(e.getMessage()).build();
        }

    }


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