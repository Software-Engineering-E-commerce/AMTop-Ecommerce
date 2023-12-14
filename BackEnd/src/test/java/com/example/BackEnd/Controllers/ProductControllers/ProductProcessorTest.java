package com.example.BackEnd.Controllers.ProductControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductServices.IProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductProcessorTest {

    @Mock
    private Permissions permissions;
    @Mock
    private IProductService productService;
    @InjectMocks
    private ProductProcessor productProcessor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    @DirtiesContext
    void processProduct_Success() throws Exception {
        // Arrange
        String jsonString = """
                {
                  "productName": "Example Product",
                  "price": 29.99,
                  "postedDate": "2023-12-11T10:30:00",
                  "description": "This is a sample product.",
                  "productCountAvailable": 100,
                  "brand": "Example Brand",
                  "category": "ELECTRONICS"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doNothing().when(productService).processProduct(any(), any());

        // Act
        ResponseEntity<String> result = productProcessor.processProduct(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(1)).processProduct(any(), any());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void nullDto() throws Exception {
        // Arrange
        String jsonString = "";
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);

        // Act
        ResponseEntity<String> result = productProcessor.processProduct(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).processProduct(any(), any());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void incorrectJson() throws Exception {
        String jsonString = """
                {
                  "x": "Example Product",
                   "y": 29.99,
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);

        // Act
        ResponseEntity<String> result = productProcessor.processProduct(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).processProduct(any(), any());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void notAdmin() throws Exception {
        // Arrange
        String jsonString = """
                {
                  "productName": "Example Product",
                  "price": 29.99,
                  "postedDate": "2023-12-11T10:30:00",
                  "description": "This is a sample product.",
                  "productCountAvailable": 100,
                  "brand": "Example Brand",
                  "category": "ELECTRONICS"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(false);

        // Act
        ResponseEntity<String> result = productProcessor.processProduct(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access"), result);

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).processProduct(any(), any());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void invalidToken() throws Exception {
        String jsonString = """
                {
                  "productName": "Example Product",
                  "price": 29.99,
                  "postedDate": "2023-12-11T10:30:00",
                  "description": "This is a sample product.",
                  "productCountAvailable": 100,
                  "brand": "Example Brand",
                  "category": "ELECTRONICS"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "invalidToken";

        when(permissions.checkToken(anyString())).thenReturn(false);

        // Act
        ResponseEntity<String> result = productProcessor.processProduct(jsonString, image, token);

        // Assert
        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token"), result);

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(0)).checkAdmin(token);
        verify(productService, times(0)).processProduct(any(), any());
    }

}