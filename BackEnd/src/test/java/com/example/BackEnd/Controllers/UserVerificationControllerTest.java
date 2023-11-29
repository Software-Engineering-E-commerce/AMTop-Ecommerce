package com.example.BackEnd.Controllers;

import com.example.BackEnd.Services.UserVerification;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserVerificationControllerTest {

    @Mock
    private UserVerification userVerification;

    @InjectMocks
    private UserVerificationController userVerificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void verifyUserSuccess() {
        String token = "validToken";
        doNothing().when(userVerification).verifyUser(token);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        ResponseEntity<String> response = userVerificationController.verifyUser(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User verified successfully", response.getBody());
    }

    @Test
    void verifyUserNotFound() {
        String token = "invalidToken";
        doThrow(new UsernameNotFoundException("User not found")).when(userVerification).verifyUser(token);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        ResponseEntity<String> response = userVerificationController.verifyUser(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void verifyUserAlreadyVerified() {
        String token = "alreadyVerifiedToken";
        doThrow(new IllegalStateException("User already verified")).when(userVerification).verifyUser(token);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        ResponseEntity<String> response = userVerificationController.verifyUser(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User already verified", response.getBody());
    }

    @Test
    void verifyNoHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);
        ResponseEntity<String> response = userVerificationController.verifyUser(request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }
}