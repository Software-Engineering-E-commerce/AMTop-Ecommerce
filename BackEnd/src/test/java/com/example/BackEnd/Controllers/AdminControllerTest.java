package com.example.BackEnd.Controllers;

import com.example.BackEnd.Services.RootAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private RootAdminService rootAdminService;
    @InjectMocks
    private AdminController adminController;

    @Test
    void testExtractTokenSuccess() {
        // Arrange
        String authorizationHeader = "Bearer mockToken";

        // Act
        String token = adminController.extractToken(authorizationHeader);

        // Assert
        assertEquals("mockToken", token);
    }

    @Test
    void testExtractTokenInvalidAuthorizationHeader() {
        // Arrange
        String authorizationHeader = "InvalidToken";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> adminController.extractToken(authorizationHeader));
    }

    @Test
    void testExtractTokenMissingAuthorizationHeader() {
        // Arrange
        String authorizationHeader = null;

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> adminController.extractToken(authorizationHeader));
    }

    @Test
    void testExtractTokenMissingBearerPrefix() {
        // Arrange
        String authorizationHeader = "mockTokenWithoutBearer";

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> adminController.extractToken(authorizationHeader));
    }

    @Test
    void testAddAdminSuccess() throws Exception {
        // Arrange
        String authorizationHeader = "Bearer mockToken";
        String newAdminEmail = "newadmin@example.com";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newAdminEmail", newAdminEmail);

        // Mock the behavior of the rootAdminService in case of valid data
        doNothing().when(rootAdminService).addEmployee(anyString(), anyString());

        // Act
        ResponseEntity<String> response = adminController.addAdmin(authorizationHeader, requestBody);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Admin has been added and a Verification Email sent to him", response.getBody());

        // Verify that rootAdminService.addEmployee is called with the correct parameters
        verify(rootAdminService, times(1)).addEmployee(eq("mockToken"), eq(newAdminEmail));
    }

    @Test
    void testAddAdminForbidden() throws Exception {
        // Arrange
        String authorizationHeader = "Bearer mockToken";
        String newAdminEmail = "newadmin@example.com";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newAdminEmail", newAdminEmail);

        // Mock the behavior of the rootAdminService to throw a Forbidden exception
        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden")).when(rootAdminService).addEmployee(anyString(), anyString());

        // Act
        ResponseEntity<String> response = adminController.addAdmin(authorizationHeader, requestBody);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Forbidden", response.getBody());
    }

    @Test
    void testAddAdminConflict() throws Exception {
        // Arrange
        String authorizationHeader = "Bearer mockToken";
        String newAdminEmail = "newadmin@example.com";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newAdminEmail", newAdminEmail);


        // Mock the behavior of the rootAdminService to throw a Conflict exception
        doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Conflict")).when(rootAdminService).addEmployee(anyString(), anyString());

        // Act
        ResponseEntity<String> response = adminController.addAdmin(authorizationHeader, requestBody);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody());
    }

    @Test
    void testAddAdminInternalServerError() throws Exception {
        // Arrange
        String authorizationHeader = "Bearer mockToken";
        String newAdminEmail = "newadmin@example.com";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newAdminEmail", newAdminEmail);

        // Mock the behavior of the rootAdminService to throw an unexpected exception
        doThrow(new RuntimeException("Internal Server Error")).when(rootAdminService).addEmployee(anyString(), anyString());

        // Act
        ResponseEntity<String> response = adminController.addAdmin(authorizationHeader, requestBody);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody());
    }

    @Test
    void testAddAdminInvalidAuthorizationHeader() {
        // Arrange
        String authorizationHeader = "InvalidToken";
        String newAdminEmail = "newadmin@example.com";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("newAdminEmail", newAdminEmail);

        // Act
        ResponseEntity<String> response = adminController.addAdmin(authorizationHeader, requestBody);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }


}