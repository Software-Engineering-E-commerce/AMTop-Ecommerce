package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Services.UserVerification;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminVerificationControllerTest {

    @Mock
    private UserVerification userVerification;

    @InjectMocks
    private AdminVerificationController adminVerificationController;


    @Test
    void testVerifyAdmin_Success() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RegisterRequest registerRequest = new RegisterRequest();
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        // Since verifyAdmin has void return type, use doNothing() for successful invocation
        doNothing().when(userVerification).verifyAdmin(any(), any());

        // Act
        ResponseEntity<String> response = adminVerificationController.verifyAdmin(request, registerRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin verified successfully", response.getBody());
    }

    @Test
    void testVerifyAdmin_Conflict() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RegisterRequest registerRequest = new RegisterRequest();
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        // Use doThrow() for void methods
        doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Conflict"))
                .when(userVerification).verifyAdmin(any(), any());

        // Act
        ResponseEntity<String> response = adminVerificationController.verifyAdmin(request, registerRequest);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody());
    }

    @Test
    void testVerifyAdmin_NotFound() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RegisterRequest registerRequest = new RegisterRequest();
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        // Use doThrow() for void methods
        doThrow(new UsernameNotFoundException("Not found"))
                .when(userVerification).verifyAdmin(any(), any());

        // Act
        ResponseEntity<String> response = adminVerificationController.verifyAdmin(request, registerRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not found", response.getBody());
    }

    @Test
    void testVerifyAdmin_BadRequest() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RegisterRequest registerRequest = new RegisterRequest();
        when(request.getHeader("Authorization")).thenReturn("Bearer mockToken");
        // Use doThrow() for void methods
        doThrow(new IllegalStateException("Bad request"))
                .when(userVerification).verifyAdmin(any(), any());

        // Act
        ResponseEntity<String> response = adminVerificationController.verifyAdmin(request, registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad request", response.getBody());
    }

    @Test
    void testVerifyAdmin_Unauthorized() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        RegisterRequest registerRequest = new RegisterRequest();
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<String> response = adminVerificationController.verifyAdmin(request, registerRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid token", response.getBody());
    }
}