package com.example.BackEnd.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import java.io.IOException;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import org.mockito.MockedStatic;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    // Add this method to clear the SecurityContextHolder after each test
    @AfterEach
    void afterEach() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_ValidToken() throws ServletException, IOException {
        // Mock SecurityContextHolder
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            SecurityContext securityContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
            when(jwtService.extractUsername("validToken")).thenReturn("user@example.com");

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

            when(jwtService.isTokenValid("validToken", userDetails)).thenReturn(true);

            // Act
            jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain, times(1)).doFilter(request, response);

            // Verify that the authentication token is set in the SecurityContextHolder
            UsernamePasswordAuthenticationToken expectedAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            // Adjust verification to ignore additional details
            verify(securityContext, times(1)).setAuthentication(withTokenIgnoringDetails(expectedAuthenticationToken));
        }
    }

    // Custom matcher to ignore details in the authentication token
    private static UsernamePasswordAuthenticationToken withTokenIgnoringDetails(UsernamePasswordAuthenticationToken expected) {
        return argThat(new ArgumentMatcher<UsernamePasswordAuthenticationToken>() {
            @Override
            public boolean matches(UsernamePasswordAuthenticationToken actual) {
                Object expectedCredentials = expected.getCredentials();
                Object actualCredentials = actual.getCredentials();

                // Check if both credentials are null or equal
                return (expectedCredentials == null && actualCredentials == null) ||
                        (expectedCredentials != null && expectedCredentials.equals(actualCredentials)) &&
                                actual.getPrincipal().equals(expected.getPrincipal()) &&
                                actual.getAuthorities().equals(expected.getAuthorities()) &&
                                actual.isAuthenticated() == expected.isAuthenticated();
            }
        });
    }

    @Test
    void doFilterInternal_InvalidToken() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(jwtService.extractUsername("invalidToken")).thenReturn("user@example.com");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(userDetails);

        when(jwtService.isTokenValid("invalidToken", userDetails)).thenReturn(false);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that there are no interactions with SecurityContextHolder
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_NoAuthorizationHeader() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);

        // Verify that SecurityContextHolder, JwtService, and UserDetailsService are not interacted with when no Authorization header is present
        verify(jwtService, never()).extractUsername(anyString());
        verify(userDetailsService, never()).loadUserByUsername(anyString());
    }
}