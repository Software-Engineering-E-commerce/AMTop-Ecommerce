package com.example.BackEnd.Services;
import com.example.BackEnd.DTO.AuthenticationResponse;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Config.JwtService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
class RegistrationServiceTests {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;
    @Mock
    private Customer testCustomer;
    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testCustomer = new Customer();
        testCustomer.setEmail("testuser@example.com");
        testCustomer.setPassword(passwordEncoder.encode("correctPassword"));
        testCustomer.setId(1L);
        testCustomer.setFirstName("testFirstName");
        testCustomer.setLastName("testLastName");
        testCustomer.setIsGmail(false);
        testCustomer.setIsVerified(true);
        customerRepository.save(testCustomer);
    }
    @AfterEach
    void tearDown() {
        customerRepository.delete(testCustomer);
    }
    @Test
    @Transactional
    @DirtiesContext
    void userRegisterWithNewValidEmail_ValidPassword() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest("newuser", "now", "John@example.com", "password");
        Mockito.when(customerRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(adminRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        Mockito.when(jwtService.generateToken(Mockito.any(Customer.class))).thenReturn("jwtToken");
        // Set up mock behavior for EmailService
        Mockito.doNothing().when(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        // Act
        AuthenticationResponse response = authenticationService.customerRegister(request);

        // Assert
        assertNotNull(response);
        assertEquals("SUCCESS", response.getToken());
    }

}
