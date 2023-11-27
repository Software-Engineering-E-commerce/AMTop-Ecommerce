package com.example.BackEnd.Services;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public boolean authenticate(String username, String password) {
        return "test".equals(username) && "test".equals(password);
    }
}
