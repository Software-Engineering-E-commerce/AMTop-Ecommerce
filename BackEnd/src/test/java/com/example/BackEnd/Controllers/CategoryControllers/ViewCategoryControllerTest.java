package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Services.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
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
    private CategoryDTO createMockProductResponse() {
        return CategoryDTO.builder()
                .name("Mock Category")
                .build();
    }

    @Test
    void testViewCategoryWithValidToken(){
        // Arrange
        String catName = "test category";
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        CategoryDTO exResponse = createMockProductResponse();
        when(categoryService.getCategory(catName)).thenReturn(exResponse);
        // Act
        ResponseEntity<CategoryDTO> responseEntity = viewCategoryController.viewCategory(request, catName);
        // Assert
        assertEquals(ResponseEntity.ok(exResponse), responseEntity);
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, times(1)).getCategory(catName);
        Objects.requireNonNull(responseEntity.getBody());
    }
    @Test
    void testViewCategoryWithInvalidToken() {
        // Arrange
        String categoryName = "test category";
        String invalidToken = "invalidToken";
        String authorizationHeader = "Bearer " + invalidToken;

        when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        when(categoryService.getCategory(categoryName)).thenReturn(null);

        // Act
        ResponseEntity<CategoryDTO> responseEntity = viewCategoryController.viewCategory(request, categoryName);

        // Assert
        assertEquals(200, responseEntity.getStatusCodeValue());
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, times(1)).getCategory(categoryName);
        assertNull(responseEntity.getBody());
    }

    @Test
    void testViewCategoryWithMissingToken() {
        // Arrange
        String categoryName = "test category";

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        ResponseEntity<CategoryDTO> responseEntity = viewCategoryController.viewCategory(request, categoryName);

        // Assert
        assertEquals(400, responseEntity.getStatusCodeValue());
        verify(request, times(1)).getHeader("Authorization");
        verify(categoryService, never()).getCategory(anyString());
        assertNull(responseEntity.getBody());
    }
    @Test
    void testGetAllCategories() {
        // Arrange
        String authorizationHeader = "Bearer yourToken";
        List<CategoryDTO> mockCategories = Collections.singletonList(new CategoryDTO("CategoryName", "ImageURL"));

        lenient().when(request.getHeader("Authorization")).thenReturn(authorizationHeader);
        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        // Act
        ResponseEntity<List<CategoryDTO>> responseEntity = viewCategoryController.getAllCategories(authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockCategories, responseEntity.getBody());
        assertNotNull(responseEntity.getBody());
        // Verify that extractToken method is indirectly called through request.getHeader("Authorization")
        Mockito.verify(request, times(0)).getHeader("Authorization");
    }
}
