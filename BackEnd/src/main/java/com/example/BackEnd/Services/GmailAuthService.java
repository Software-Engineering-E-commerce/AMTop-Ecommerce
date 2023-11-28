package com.example.BackEnd.Services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
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
public class GmailAuthService {

    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;

    public AuthenticationResponse customerRegister(String token) {
        DecodedJWT jwt = JWT.decode(token);
        //extract information from the token
        String email = jwt.getClaim("email").asString();
        String firstName = jwt.getClaim("given_name").asString();
        String lastName = jwt.getClaim("family_name").asString();

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);

        if(optionalCustomer.isPresent()){
            Customer customer = optionalCustomer.get();
            if(!customer.getIsGmail()){
                return AuthenticationResponse.builder().token("Already Exists").build();
            }
            var jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }else if (optionalAdmin.isPresent()){
            Admin admin = optionalAdmin.get();
            if(!admin.getIsGmail()){
                return AuthenticationResponse.builder().token("Already Exists").build();
            }
            var jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }else{
            Customer customer = new Customer(email, null, true, true, firstName, lastName);
            customerRepository.save(customer);
            var jwtToken = jwtService.generateToken(customer);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }
}
