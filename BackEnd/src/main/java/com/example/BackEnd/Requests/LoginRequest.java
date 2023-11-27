package com.example.BackEnd.Requests;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String username;
    private String password;


    // Constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter and Setter methods

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}