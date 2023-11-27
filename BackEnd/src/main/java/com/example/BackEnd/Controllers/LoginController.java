package com.example.BackEnd.Controllers;

import com.example.BackEnd.Requests.LoginRequest;
import com.example.BackEnd.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequest loginRequest) {

        boolean isAuthenticated = loginService.authenticate(loginRequest.getUsername(),
                                                            loginRequest.getPassword());
        if (isAuthenticated) return new ResponseEntity<>("Success", HttpStatus.OK);
        else return new ResponseEntity<>("Fail", HttpStatus.UNAUTHORIZED);
    }
}
