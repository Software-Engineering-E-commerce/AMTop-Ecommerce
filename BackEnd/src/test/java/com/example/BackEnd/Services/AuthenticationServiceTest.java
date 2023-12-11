package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.LoginRequest;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class AuthenticationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Customer testCustomer, unverifiedTestCustomer, gmailTestCustomer;

    private Admin testAdmin, unverifiedTestAdmin, gmailTestAdmin;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setEmail("testcustomer@example.com");
        testCustomer.setPassword(passwordEncoder.encode("customerCorrectPassword"));
        testCustomer.setId(999995L);
        testCustomer.setFirstName("testFirstName");
        testCustomer.setLastName("testLastName");
        testCustomer.setIsGmail(false);
        testCustomer.setIsVerified(true);
        customerRepository.save(testCustomer);

        unverifiedTestCustomer = new Customer();
        unverifiedTestCustomer.setEmail("utestcustomer@example.com");
        unverifiedTestCustomer.setPassword(passwordEncoder.encode("uCustomerCorrectPassword"));
        unverifiedTestCustomer.setId(999996L);
        unverifiedTestCustomer.setFirstName("testFirstName");
        unverifiedTestCustomer.setLastName("testLastName");
        unverifiedTestCustomer.setIsGmail(false);
        unverifiedTestCustomer.setIsVerified(false);
        customerRepository.save(unverifiedTestCustomer);

        gmailTestCustomer = new Customer();
        gmailTestCustomer.setEmail("gtestcustomer@example.com");
        gmailTestCustomer.setPassword(passwordEncoder.encode("gCustomerCorrectPassword"));
        gmailTestCustomer.setId(999997L);
        gmailTestCustomer.setFirstName("testFirstName");
        gmailTestCustomer.setLastName("testLastName");
        gmailTestCustomer.setIsGmail(true);
        gmailTestCustomer.setIsVerified(true);
        customerRepository.save(gmailTestCustomer);

        testAdmin = new Admin();
        testAdmin.setEmail("testadmin@example.com");
        testAdmin.setPassword(passwordEncoder.encode("adminCorrectPassword"));
        testAdmin.setId(999995L);
        testAdmin.setFirstName("testFirstName");
        testAdmin.setLastName("testLastName");
        testAdmin.setIsGmail(false);
        testAdmin.setIsVerified(true);
        adminRepository.save(testAdmin);

        unverifiedTestAdmin = new Admin();
        unverifiedTestAdmin.setEmail("utestadmin@example.com");
        unverifiedTestAdmin.setPassword(passwordEncoder.encode("uAdminCorrectPassword"));
        unverifiedTestAdmin.setId(999996L);
        unverifiedTestAdmin.setFirstName("testFirstName");
        unverifiedTestAdmin.setLastName("testLastName");
        unverifiedTestAdmin.setIsGmail(false);
        unverifiedTestAdmin.setIsVerified(false);
        adminRepository.save(unverifiedTestAdmin);

        gmailTestAdmin = new Admin();
        gmailTestAdmin.setEmail("gtestadmin@example.com");
        gmailTestAdmin.setPassword(passwordEncoder.encode("gAdminCorrectPassword"));
        gmailTestAdmin.setId(999997L);
        gmailTestAdmin.setFirstName("testFirstName");
        gmailTestAdmin.setLastName("testLastName");
        gmailTestAdmin.setIsGmail(true);
        gmailTestAdmin.setIsVerified(true);
        adminRepository.save(gmailTestAdmin);
    }

    @AfterEach
    void tearDown() {
        customerRepository.delete(testCustomer);
        customerRepository.delete(unverifiedTestCustomer);
        customerRepository.delete(gmailTestCustomer);
        adminRepository.delete(testAdmin);
        adminRepository.delete(unverifiedTestAdmin);
        adminRepository.delete(gmailTestAdmin);
    }

    @Test
    void whenCustomerInvalidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("wronguser@example.com", "customerCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCustomerValidEmailAndValidPassword_thenAuthorized() throws Exception {
        LoginRequest request = new LoginRequest(testCustomer.getEmail(), "customerCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void whenCustomerValidEmailAndInvalidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(testCustomer.getEmail(), "wrongPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCustomerInvalidEmailAndInvalidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("wronguser@example.com", "wrongPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCustomerEmailIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(null, "customerCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCustomerPasswordIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(testCustomer.getEmail(), null);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenCustomerAllIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(null, null);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminInvalidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("wronguser@example.com", "adminCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminValidEmailAndValidPassword_thenAuthorized() throws Exception {
        LoginRequest request = new LoginRequest(testAdmin.getEmail(), "adminCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void whenAdminValidEmailAndInvalidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(testAdmin.getEmail(), "wrongPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminInvalidEmailAndInvalidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("wronguser@example.com", "wrongPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminEmailIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(null, "adminCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminPasswordIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(testAdmin.getEmail(), null);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenAdminAllIsNull_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(null, null);

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenUnverifiedCustomerValidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(unverifiedTestCustomer.getEmail(), "uCustomerCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenGmailCustomerValidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(gmailTestCustomer.getEmail(), "gCustomerCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenUnverifiedAdminValidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(unverifiedTestAdmin.getEmail(), "uAdminCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenGmailAdminValidEmailAndValidPassword_thenUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest(gmailTestAdmin.getEmail(), "gAdminCorrectPassword");

        mockMvc.perform(post("/api/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

}