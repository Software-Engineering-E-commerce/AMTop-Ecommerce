package com.example.BackEnd.Controllers;

import com.example.BackEnd.Services.RootAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class AdminController {
    private final RootAdminService rootAdminService;

    @PostMapping("/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("newAdminEmail") String newAdminEmail) {
        try {
            String token = extractToken(authorizationHeader);
            rootAdminService.addEmployee(token, newAdminEmail);
            return ResponseEntity.ok("Admin has been added and a Verification Email sent to him");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7); // Skip "Bearer " prefix
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }
}
