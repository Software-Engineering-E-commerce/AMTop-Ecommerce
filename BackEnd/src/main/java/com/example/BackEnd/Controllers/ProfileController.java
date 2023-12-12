package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.UserProfileDTO;
import com.example.BackEnd.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final JwtService jwtService;

    @GetMapping("/home/profile")
    public ResponseEntity<UserProfileDTO> retrieve(@RequestHeader("Authorization") String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.retrieveData(email));
        } else {
            return null;
        }
    }

    @PostMapping("/home/updateProfile")
    public ResponseEntity<String> updateData(@RequestHeader("Authorization") String authorizationHeader,
                                     @RequestBody UserProfileDTO userProfileDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return ResponseEntity.ok(service.updateData(userProfileDTO ,email));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        }
    }
}

