package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserVerificationTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserVerification userVerification;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void verifyUser_WhenUserNotVerified_ShouldSetIsVerifiedTrue() {
        // Arrange
        String token = "sampleToken";
        String username = "sampleUser";

        Customer customer = new Customer();
        customer.setEmail(username);
        customer.setIsVerified(false);

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(customerRepository.findByEmail(username)).thenReturn(Optional.of(customer));

        // Act
        userVerification.verifyUser(token);

        // Assert
        verify(customerRepository, times(1)).save(customer);
        assertEquals(true, customer.getIsVerified());
    }

    @Test
    void verifyUser_WhenUserAlreadyVerified_ShouldThrowIllegalStateException() {
        // Arrange
        String token = "sampleToken";
        String username = "sampleUser";

        Customer customer = new Customer();
        customer.setEmail(username);
        customer.setIsVerified(true);

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(customerRepository.findByEmail(username)).thenReturn(Optional.of(customer));

        // Act and Assert
        assertThrows(IllegalStateException.class, () -> userVerification.verifyUser(token));

        // Verify that save method is not called
        verify(customerRepository, never()).save(customer);
        assertEquals(true, customer.getIsVerified());
        assertThrows(IllegalStateException.class, () -> userVerification.verifyUser(token));
    }

    @Test
    void verifyUser_WhenUserNotFound_ShouldThrowUsernameNotFoundException() {
        // Arrange
        String token = "sampleToken";
        String username = "nonExistentUser";

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(customerRepository.findByEmail(username)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(UsernameNotFoundException.class, () -> userVerification.verifyUser(token));

        // Verify that save method is not called
        verify(customerRepository, never()).save(any());
        assertThrows(UsernameNotFoundException.class, () -> userVerification.verifyUser(token));
    }

    @Test
    void testVerifyAdmin_Success() {
        // Arrange
        String token = "mockToken";
        String email = "admin@example.com";
        RegisterRequest registerRequest = new RegisterRequest("mah", "att", email, "1234");

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setIsVerified(false);

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Act
        userVerification.verifyAdmin(token, registerRequest);

        // Assert
        verify(adminRepository, times(1)).save(any());
    }

    @Test
    void testVerifiedAdmin() {
        // Arrange
        String token = "mockToken";
        String email = "admin@example.com";
        RegisterRequest registerRequest = new RegisterRequest("mah", "att", email, "1234");

        Admin admin = new Admin();
        admin.setEmail(email);
        admin.setIsVerified(true);

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(admin));

        // Act and Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> userVerification.verifyAdmin(token, registerRequest));
        assertEquals("Admin already verified", exception.getMessage());
    }

    @Test
    void testVerifyAdmin_Conflict() {
        // Arrange
        String token = "mockToken";
        String email1 = "email1", email2 = "email2";
        RegisterRequest registerRequest = new RegisterRequest("mah", "att", email1, "1234");

        when(jwtService.extractUsername(token)).thenReturn(email2);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> userVerification.verifyAdmin(token, registerRequest));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("The provided form mail doesn't match the verification mail sent", exception.getReason());
    }

    @Test
    void testVerifyAdmin_AdminNotFound() {
        // Arrange
        String token = "mockToken";
        String email = "admin@example.com";
        RegisterRequest registerRequest = new RegisterRequest("mah", "att", email, "1234");

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act and Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userVerification.verifyAdmin(token, registerRequest));
        assertEquals("Admin not found", exception.getMessage());
    }
}