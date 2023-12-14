package com.example.BackEnd.Controllers;
import com.example.BackEnd.Services.UserVerification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/verification")
@CrossOrigin(origins = "http://localhost:3000/")
public class UserVerificationController {

    private final UserVerification userService;

    @GetMapping
    public ResponseEntity<String> verifyUser(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7) {
            String token = authorizationHeader.substring(7);
            try {
                userService.verifyUser(token);
                return ResponseEntity.status(HttpStatus.OK).body("User verified successfully");
            } catch (UsernameNotFoundException e) { // user not found
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } catch (IllegalStateException e) { // user already verified
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
