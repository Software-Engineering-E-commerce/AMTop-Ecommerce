package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AddCategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private Permissions permissions;

    @InjectMocks
    private AddCategoryController addCategoryController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddCategory_Success() throws Exception {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(true);
        when(categoryService.getSuccessMessage(true)).thenReturn("Category added successfully");

        // Call the controller method
        ResponseEntity<String> responseEntity = addCategoryController.addCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Category added successfully", responseEntity.getBody());
    }

    @Test
    public void testAddCategory_Unauthorized() throws Exception {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer InvalidToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(false);

        // Call the controller method
        ResponseEntity<String> responseEntity = addCategoryController.addCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Invalid token", responseEntity.getBody());
    }

    @Test
    public void testAddCategory_UnauthorizedAdmin() throws Exception {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(false);

        // Call the controller method
        ResponseEntity<String> responseEntity = addCategoryController.addCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(401, responseEntity.getStatusCodeValue());
        assertEquals("Unauthorized access", responseEntity.getBody());
    }

    @Test
    public void testAddCategory_InternalServerError() throws Exception {
        // Mock data
        String jsonString = "{\"name\": \"TestCategory\"}";
        MultipartFile mockImage = Mockito.mock(MultipartFile.class);
        String authorizationHeader = "Bearer AdminToken";

        // Mock the behavior of your dependencies
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("AdminToken")).thenReturn(true);
        when(categoryService.getSuccessMessage(true)).thenThrow(new RuntimeException("Internal Server Error"));

        // Call the controller method
        ResponseEntity<String> responseEntity = addCategoryController.addCategory(jsonString, mockImage, authorizationHeader);

        // Verify the result
        assertEquals(500, responseEntity.getStatusCodeValue());
        assertEquals("Internal Server Error", responseEntity.getBody());
    }
}

