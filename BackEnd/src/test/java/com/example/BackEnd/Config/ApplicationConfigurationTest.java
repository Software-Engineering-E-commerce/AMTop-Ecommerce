package com.example.BackEnd.Config;

import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ApplicationConfigurationTest {

    // Injecting mocks into the ApplicationConfiguration class.
    @InjectMocks
    private ApplicationConfiguration applicationConfiguration;

    // Creating mock repositories for Admin and Customer entities.
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    // Testing the userDetailsService method of ApplicationConfiguration.
    @Test
    void userDetailsService() {
        // Setting up test data.
        String testUsername = "testUser";
        Customer testCustomer = new Customer();
        testCustomer.setEmail(testUsername);

        // Mocking repository responses.
        when(customerRepository.findByEmail(testUsername)).thenReturn(Optional.of(testCustomer));
        when(adminRepository.findByEmail(testUsername)).thenReturn(Optional.empty());

        // Calling the userDetailsService and testing its result.
        UserDetailsService userDetailsService = applicationConfiguration.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUsername);

        assertNotNull(userDetails);
        assertEquals(testUsername, userDetails.getUsername());
    }

    // Testing the case where the user is not found in both repositories.
    @Test
    void userDetailsService_UserNotFound() {
        // Setting up test data.
        String testUsername = "nonExistingUser";

        // Mocking repository responses for a non-existing user.
        when(customerRepository.findByEmail(testUsername)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(testUsername)).thenReturn(Optional.empty());

        // Calling the userDetailsService and expecting a UsernameNotFoundException.
        UserDetailsService userDetailsService = applicationConfiguration.userDetailsService();
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(testUsername));
    }

    // Testing the authenticationProvider method of ApplicationConfiguration.
    @Test
    void authenticationProvider() {
        // Getting necessary components from the configuration.
        UserDetailsService userDetailsService = applicationConfiguration.userDetailsService();
        PasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();

        // Creating and configuring the authentication provider.
        DaoAuthenticationProvider authenticationProvider = (DaoAuthenticationProvider) applicationConfiguration.authenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        // Asserting that the authentication provider is not null.
        assertNotNull(authenticationProvider);
    }

    // Testing the passwordEncoder method of ApplicationConfiguration.
    @Test
    void passwordEncoder() {
        // Getting the password encoder from the configuration.
        PasswordEncoder passwordEncoder = applicationConfiguration.passwordEncoder();

        // Asserting that the password encoder is not null and can encode and match passwords.
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder.matches("testPassword", passwordEncoder.encode("testPassword")));
    }
}