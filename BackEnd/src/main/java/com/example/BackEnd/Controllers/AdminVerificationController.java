package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Services.UserVerification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/adminVerification")
@CrossOrigin(origins = "http://localhost:3000/")
public class AdminVerificationController {
    private final UserVerification userVerification;

    @GetMapping
    public ResponseEntity<String> verifyAdmin(HttpServletRequest request, @RequestBody RegisterRequest registerRequest) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7) {
            String token = authorizationHeader.substring(7);
            try {
                userVerification.verifyAdmin(token, registerRequest);
                return ResponseEntity.status(HttpStatus.OK).body("Admin verified successfully");
            } catch (ResponseStatusException e) { //request email doesn't match the token
                return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
            } catch (UsernameNotFoundException e) { // user not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (IllegalStateException e) { // user already verified
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
