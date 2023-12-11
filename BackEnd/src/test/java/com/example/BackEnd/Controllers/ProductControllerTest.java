package com.example.BackEnd.Controllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private Permissions permissions;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void addProduct_Success() throws Exception {
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

        when(permissions.checkToken(anyString())).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doNothing().when(productService).addProduct(any(), any());

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                        .file("image", image.getBytes())
                        .param("productDTO", jsonString)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added successfully"))
                .andReturn();

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(1)).addProduct(any(), any());
    }

    @Test
    public void nullDto() throws Exception {
        String jsonString = "";
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";

        when(permissions.checkToken(anyString())).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                        .file("image", image.getBytes())
                        .param("productDTO", jsonString)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andReturn();

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).addProduct(any(), any());
    }

    @Test
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

        when(permissions.checkToken(anyString())).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                        .file("image", image.getBytes())
                        .param("productDTO", jsonString)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isInternalServerError())
                .andReturn();

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).addProduct(any(), any());
    }

    @Test
    public void notAdmin() throws Exception {
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

        when(permissions.checkToken(anyString())).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(false);

        // Act
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                        .file("image", image.getBytes())
                        .param("productDTO", jsonString)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized access"))
                .andReturn();

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(productService, times(0)).addProduct(any(), any());
    }

    @Test
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
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/product/addProduct")
                        .file("image", image.getBytes())
                        .param("productDTO", jsonString)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid token"))
                .andReturn();

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(0)).checkAdmin(token);
        verify(productService, times(0)).addProduct(any(), any());
    }

    // Add more test cases for different scenarios (e.g., unauthorized, invalid token, etc.)
}
