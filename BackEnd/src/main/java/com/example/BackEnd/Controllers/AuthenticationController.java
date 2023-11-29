package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.AuthenticationResponse;

import com.example.BackEnd.DTO.LoginRequest;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/registerCustomer")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        System.out.println(request.getFirstName());
        System.out.println(request.getLastName());
        System.out.println(request.getEmail());
        System.out.println(request.getPassword());
        return ResponseEntity.ok(service.customerRegister(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
     ResponseEntity<AuthenticationResponse> token = ResponseEntity.ok(service.authenticate(request));
        return token;
    }


}