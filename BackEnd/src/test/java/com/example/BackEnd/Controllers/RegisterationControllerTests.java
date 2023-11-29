package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.LoginRequest;
import com.example.BackEnd.DTO.RegisterRequest;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class RegistrationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Mock
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Customer testCustomer;

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
    public void NewValidEmailAndValidPassword() throws Exception {

        RegisterRequest request = new RegisterRequest(testCustomer.getFirstName(),testCustomer.getLastName(),"testCustomer2@example.com","correctPassword");
        Mockito.doNothing().when(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        mockMvc.perform(post("/api/auth/registerC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    @Test
    @Transactional
    @DirtiesContext
    public void ExistingValidEmailAndValidPassword() throws Exception {

        RegisterRequest request = new RegisterRequest("kassed","kareem","testuser@example.com","12345");
        Mockito.doNothing().when(emailService).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());
        MvcResult mvcResult = mockMvc.perform(post("/api/auth/registerC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        // Now you can assert or inspect the response content as needed
        System.out.println("Response Content: " + responseContent);
        String x = responseContent.substring(responseContent.indexOf(':'));
        x=x.substring(2,15);
        assertEquals("Already Exist",x);
    }
}