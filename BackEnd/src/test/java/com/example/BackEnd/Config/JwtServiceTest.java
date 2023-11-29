package com.example.BackEnd.Config;

import com.example.BackEnd.Config.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    // Secret key for testing purposes
    private final String secretKey = "z4T1Rd5lrunK3Bk5vy48gxlr0GzQ4Z2tvDwESHDP0X2zmaV4dml5cygRWhTB8Tee";

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setUp() {
        // Mocking behavior for userDetailsService
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(userDetails);
    }

    @Test
    public void testGenerateToken() {
        // Creating a new JwtService instance for testing
        JwtService jwtService = new JwtService();

        // Loading user details for the testUser
        UserDetails userDetails = this.userDetailsService.loadUserByUsername("testUser");

        // Generating a token and checking if it's valid
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenValid() {
        // Generating a token and checking if it's valid
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    public void testIsTokenExpired() {
        // Generating a token and checking if it's not expired
        String token = jwtService.generateToken(userDetails);
        assertFalse(jwtService.isTokenExpired(token));
    }

    @Test
    public void testExtractUsername() {
        // Generating a token and extracting the username
        String token = jwtService.generateToken(userDetails);
        assertEquals("testUser", jwtService.extractUsername(token));
    }

    @Test
    public void testExtractExpiration() {
        // Generating a token and checking if the expiration date is in the future
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.extractExpiration(token).after(new Date()));
    }

    @Test
    public void testGetSigningKey() {
        // Getting the signing key and checking if it matches the expected key
        Key key = jwtService.getSigningKey();
        assertEquals(key, Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)));
    }
}
