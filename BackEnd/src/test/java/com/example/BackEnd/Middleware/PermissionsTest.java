package com.example.BackEnd.Middleware;

import static org.junit.jupiter.api.Assertions.*;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionsTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private Permissions permissions;

    @Test
    public void testCheckAdminSuccess() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("admin@example.com");
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(new Admin()));

        // Act
        boolean result = permissions.checkAdmin(token);

        // Assert
        assertTrue(result);
        verify(jwtService, times(1)).extractUsername(token);
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testCheckAdminFail() {
        // Arrange
        String token = "invalid_token";
        when(jwtService.extractUsername(token)).thenReturn("notAdmin@example.com");
        when(adminRepository.findByEmail("notAdmin@example.com")).thenReturn(Optional.empty());

        // Act
        boolean result = permissions.checkAdmin(token);

        // Assert
        assertFalse(result);
        verify(jwtService, times(1)).extractUsername(token);
        verify(adminRepository, times(1)).findByEmail("notAdmin@example.com");
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testCheckCustomerSuccess() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("customer@example.com");
        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(new Customer()));

        // Act
        boolean result = permissions.checkCustomer(token);

        // Assert
        assertTrue(result);
        verify(jwtService, times(1)).extractUsername(token);
        verify(customerRepository, times(1)).findByEmail("customer@example.com");
        verify(adminRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testCheckCustomerFail() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("notCustomer@example.com");
        when(customerRepository.findByEmail("notCustomer@example.com")).thenReturn(Optional.empty());

        // Act
        boolean result = permissions.checkCustomer(token);

        // Assert
        assertFalse(result);
        verify(jwtService, times(1)).extractUsername(token);
        verify(customerRepository, times(1)).findByEmail("notCustomer@example.com");
        verify(adminRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testCheckAdminOrCustomer_Admin() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("admin@example.com");
        when(adminRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(new Admin()));

        // Act
        boolean result = permissions.checkAdminOrCustomer(token);

        // Assert
        assertTrue(result);
        verify(jwtService, times(1)).extractUsername(token);
        verify(adminRepository, times(1)).findByEmail("admin@example.com");
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    public void testCheckAdminOrCustomer_Customer() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("customer@example.com");
        when(adminRepository.findByEmail("customer@example.com")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("customer@example.com")).thenReturn(Optional.of(new Customer()));

        // Act
        boolean result = permissions.checkAdminOrCustomer(token);

        // Assert
        assertTrue(result);
        verify(jwtService, times(2)).extractUsername(token);
        verify(adminRepository, times(1)).findByEmail("customer@example.com");
        verify(customerRepository, times(1)).findByEmail("customer@example.com");
    }

    @Test
    public void testCheckAdminOrCustomer_Neither() {
        // Arrange
        String token = "valid_token";
        when(jwtService.extractUsername(token)).thenReturn("unknown@example.com");
        when(adminRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        when(customerRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        // Act
        boolean result = permissions.checkAdminOrCustomer(token);

        // Assert
        assertFalse(result);
        verify(jwtService, times(2)).extractUsername(token);
        verify(adminRepository, times(1)).findByEmail("unknown@example.com");
        verify(customerRepository, times(1)).findByEmail("unknown@example.com");
    }

    @Test
    public void testCheckToken_Valid() {
        // Arrange
        String authorizationHeader = "Bearer valid_token";

        // Act
        boolean result = permissions.checkToken(authorizationHeader);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckToken_Invalid() {
        // Arrange
        String authorizationHeader = "InvalidToken";

        // Act
        boolean result = permissions.checkToken(authorizationHeader);

        // Assert
        assertFalse(result);
    }
}
