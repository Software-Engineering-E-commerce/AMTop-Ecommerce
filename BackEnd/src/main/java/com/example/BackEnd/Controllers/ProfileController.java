package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.UserProfileDTO;
import com.example.BackEnd.Services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService service;
    private final JwtService jwtService;

    @GetMapping("/home/profile")
    public UserProfileDTO retrieve(@RequestHeader("Authorization") String authorizationHeader){
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return service.retrieveData(email);
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @PostMapping("/home/updateProfile")
    public String updateData(@RequestHeader("Authorization") String authorizationHeader,
                                     @RequestBody UserProfileDTO userProfileDTO) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            return service.updateData(userProfileDTO, email);
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }
}

