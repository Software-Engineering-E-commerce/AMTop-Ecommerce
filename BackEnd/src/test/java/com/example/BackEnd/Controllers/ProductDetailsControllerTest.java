package com.example.BackEnd.Controllers;

import com.example.BackEnd.DTO.ProductResponse;
import com.example.BackEnd.Services.ProductDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailsControllerTest {

    @Mock
    private ProductDetailsService productDetailsService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ProductDetailsController productDetailsController;

    @Test
    void testViewProductWithValidToken() {
        // Arrange
        Long productId = 1L;
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);

        ProductResponse expectedResponse = createMockProductResponse();
        when(productDetailsService.getProduct(productId, token)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<ProductResponse> responseEntity = productDetailsController.viewProduct(request, productId);

        // Assert
        assertEquals(ResponseEntity.ok(expectedResponse), responseEntity);
        verify(request, times(1)).getHeader("Authorization");
        verify(productDetailsService, times(1)).getProduct(productId, token);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isAdmin());
    }


    @Test
    void testViewProduct_WithoutAuthorizationHeader() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long productId = 1L;

        // Act
        ResponseEntity<ProductResponse> response = productDetailsController.viewProduct(request, productId);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testViewProduct_WithInvalidToken() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        Long productId = 1L;
        String token = "InValidToken";
        String authorizationHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);

        // Mock behavior of productDetailsService.getProduct()
        when(productDetailsService.getProduct(eq(productId), eq(token))).thenReturn(null);

        // Act
        ResponseEntity<ProductResponse> response = productDetailsController.viewProduct(request, productId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
    private ProductResponse createMockProductResponse() {
        return ProductResponse.builder()
                .id(1L)
                .name("Mock Product")
                .imageUrl("mock_image_url")
                .description("Mock product description")
                .price(99.99f)
                .postedDate("2023-01-01 12:00")
                .productCountAvailable(10)
                .productCountSold(5)
                .brand("Mock Brand")
                .discountPercentage(10.0f)
                .categoryName("Mock Category")
                .categoryUrl("mock_category_url")
                .reviews(Collections.emptyList())
                .isAdmin(true)
                .isInWishlist(false)
                .build();
    }

}
