package com.example.BackEnd;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.BackEnd.Services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/googleAuth")
@RequiredArgsConstructor
public class gmailController {
    private final GmailAuthService service;
    @PostMapping("/googleRegister")
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        return ResponseEntity.ok(service.customerRegister(token));
    }
}
