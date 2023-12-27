package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EditCategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private Permissions permissions;

    @InjectMocks
    private EditCategoryController editCategoryController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEditCategory_Success() {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(true);
        when(categoryService.getSuccessMessage(false)).thenReturn("Category edited successfully");

        // Call the controller method
        ResponseEntity<String> responseEntity = editCategoryController.editCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Category edited successfully", responseEntity.getBody());
    }

    @Test
    public void testEditCategory_Unauthorized() {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer InvalidToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(false);

        // Call the controller method
        ResponseEntity<String> responseEntity = editCategoryController.editCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Invalid token", responseEntity.getBody());
    }

    @Test
    public void testEditCategory_UnauthorizedAdmin() {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(false);

        // Call the controller method
        ResponseEntity<String> responseEntity = editCategoryController.editCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Unauthorized access", responseEntity.getBody());
    }

    @Test
    public void testEditCategory_InternalServerError() {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(true);
        when(categoryService.getSuccessMessage(false)).thenThrow(new RuntimeException("Internal Server Error"));

        // Call the controller method
        ResponseEntity<String> responseEntity = editCategoryController.editCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertEquals("Internal Server Error", responseEntity.getBody());
    }
}

