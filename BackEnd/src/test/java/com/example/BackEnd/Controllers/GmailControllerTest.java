package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.AuthenticationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String validJwtToken;

    @BeforeEach
    public void setUp() {
        validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ld3VzZXJAZ29vZ2xlLmNvbSIsImdpdmVuX25hbWUiOiJOZXciLCJmYW1p" +
                "bHlfbmFtZSI6IlVzZXIifQ.brcwayiUvJSsTWLDBhXQWtKCQ7SLgjM8IUnYSZIhlLc";
    }

    @Test
    public void whenAuthenticateWithToken_thenReturnsResponse() throws Exception {
        String requestBody = "{\"token\":\"" + validJwtToken + "\"}";

        MvcResult result = mockMvc.perform(post("/googleAuth/googleRegister")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AuthenticationResponse responseObj = objectMapper.readValue(response, AuthenticationResponse.class);

        assertNotNull(responseObj);
        assertNotNull(responseObj.getToken());
    }
    @Test
    public void checkThatTokenIsValid() throws Exception {
        String requestBody = "{\"token\":\"" + validJwtToken + "\"}";

        MvcResult result = mockMvc.perform(post("/googleAuth/googleRegister")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        AuthenticationResponse responseObj = objectMapper.readValue(response, AuthenticationResponse.class);

        assertNotNull(responseObj);
        String returnedToken = responseObj.getToken();
        assertNotNull(returnedToken);

        // Check that the token structure is valid (two dots separating three base64-encoded parts)
        assertTrue(returnedToken.matches("^[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_=]+\\.[A-Za-z0-9-_.+/=]*$"));

        // Check that the key name is "token"
        assertTrue(response.contains("\"token\""));
    }
}
