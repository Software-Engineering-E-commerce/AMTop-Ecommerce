package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.DTO.CategoryResponse;
import com.example.BackEnd.Services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewCategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private ViewCategoryController viewCategoryController;
    private CategoryResponse createMockProductResponse() {
        return CategoryResponse.builder()
                .name("Mock Category")
                .isAdmin(true)
                .build();
    }

    @Test
    void testViewCategoryWithValidToken(){
        // Arrange
        String catName = "test category";
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        CategoryResponse exResponse = createMockProductResponse();
        when(categoryService.getCategory(catName,token)).thenReturn(exResponse);
        // Act
        ResponseEntity<CategoryResponse> responseEntity = viewCategoryController.viewCategory(request, catName);
        // Assert
        assertEquals(ResponseEntity.ok(exResponse), responseEntity);
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, times(1)).getCategory(catName, token);
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isAdmin());
    }
    @Test
    void testViewCategoryWithInvalidToken() {
        // Arrange
        String categoryName = "test category";
        String invalidToken = "invalidToken";
        String authorizationHeader = "Bearer " + invalidToken;

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        when(categoryService.getCategory(categoryName, invalidToken)).thenReturn(null);

        // Act
        ResponseEntity<CategoryResponse> responseEntity = viewCategoryController.viewCategory(request, categoryName);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, times(1)).getCategory(categoryName, invalidToken);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testViewCategoryWithMissingToken() {
        // Arrange
        String categoryName = "test category";

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<CategoryResponse> responseEntity = viewCategoryController.viewCategory(request, categoryName);

        // Assert
        assertEquals(400, responseEntity.getStatusCodeValue());
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, never()).getCategory(anyString(), anyString());
        assertNull(responseEntity.getBody());
    }
}
