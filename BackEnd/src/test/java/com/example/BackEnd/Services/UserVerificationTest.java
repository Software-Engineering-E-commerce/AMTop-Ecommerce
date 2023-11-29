package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserVerificationTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private UserVerification userVerification;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

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
}