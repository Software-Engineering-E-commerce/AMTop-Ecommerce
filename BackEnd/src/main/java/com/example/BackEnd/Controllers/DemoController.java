package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/demo")
public class DemoController {
    private final JwtService jwtService;
    @GetMapping
    public ResponseEntity<String> sayHello(HttpServletRequest request) {
        // Extract the token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            // Now 'token' contains your bearer token, you can use it as needed
            // For example, you can validate the token or extract information from it
            System.out.println(jwtService.extractUsername(token));
            return ResponseEntity.ok("Hello from secured endpoint. Bearer Token: " + token);
        } else {
            return ResponseEntity.badRequest().body("Invalid or missing Authorization header");
        }
    }

}