package com.example.BackEnd.Controllers.CategoryControllers;

import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Services.CategoryService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryProcessorTest {
    @Mock
    private Permissions permissions;
    @Mock
    private CategoryService categoryService;
    @InjectMocks
    private CategoryProcessor categoryProcessor;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //Test for successfully adding category
    @Test
    @Transactional
    @DirtiesContext
    void addCategory_Success() throws Exception {
        // Arrange
        String jsonString = """
                {
                  "name": "Example Category"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doNothing().when(categoryService).addCategory(any(), any());
        // Act
        ResponseEntity<String> result = categoryProcessor.addCategory(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(categoryService, times(1)).addCategory(any(), any());
    }

    @Test
    void addCategory_SuccessfulTest() throws Exception {
        // Arrange
        String jsonString = "{}";
        MultipartFile image = mock(MultipartFile.class);
        String authorizationHeader = "Bearer token";

        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("token")).thenReturn(true);
        when(categoryService.getSuccessMessage(true)).thenReturn("Category added successfully");

        // Act
        ResponseEntity<String> response = categoryProcessor.addCategory(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Category added successfully", response.getBody());

        // Verify that the necessary methods were called
        verify(categoryService, times(1)).addCategory(any(), any());
    }
    //---------------------------------------------------------------------------------------------------

    // test for unauthorized access for add category
    @Test
    void addCategory_UnauthorizedAccessTest() throws Exception {
        // Arrange
        String jsonString = "{}";
        MultipartFile image = mock(MultipartFile.class);
        String authorizationHeader = "Bearer token";

        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin("token")).thenReturn(false);

        // Act
        ResponseEntity<String> response = categoryProcessor.addCategory(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized access", response.getBody());

        verify(categoryService, never()).addCategory(any(), any());
    }
    //---------------------------------------------------------------------------------------------------------------

    //test for error handling for add category
    @Test
    void addCategory_ErrorAddingCategoryTest() throws Exception {
        // Arrange
        String jsonString = """
                {
                  "name": "Example Category"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doThrow(new RuntimeException("Error adding category")).when(categoryService).addCategory(any(), any());

        // Act
        ResponseEntity<String> result = categoryProcessor.addCategory(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Error adding category", result.getBody());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(categoryService, times(1)).addCategory(any(), any());
    }
    //--------------------------------------------------------------------------------------------------------------------

    // test on invalid token for add category
    @Test
    void addCategory_InvalidTokenTest() throws Exception {
        // Arrange
        String jsonString = "{\"name\": \"New Category\"}";
        MultipartFile image = mock(MultipartFile.class);
        String token = "invalidToken"; // Assuming an invalid token
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(false);

        // Act
        ResponseEntity<String> result = categoryProcessor.addCategory(jsonString, image, authorizationHeader);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, never()).checkAdmin(token);
        verify(categoryService, never()).addCategory(any(), any());
    }
    //-------------------------------------------------------------------------------------------------------------------------

    //Test for editing category successfully test
    @Test
    void editCategory_SuccessfulTest() throws Exception {
        // Arrange
        String jsonString = """
                {
                  "name": "Example Category"
                }
                """;
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String orgName = "original";
        String authorizationHeader = "Bearer " + token;
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doNothing().when(categoryService).editCategory(any(), any(), any());

        // Act
        ResponseEntity<String> result = categoryProcessor.editCategory(jsonString, image, authorizationHeader, orgName);

        // Assert
        assertEquals(HttpStatus.OK, result.getStatusCode());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(categoryService, times(1)).editCategory(any(), any(), any());
    }
    //---------------------------------------------------------------------------------------------------------------

    //test for unauthorized access for edit category
    @Test
    void editCategory_UnauthorizedAccessTest() throws Exception {
        // Arrange
        String jsonString = "{\"name\": \"Updated Category\"}";
        MultipartFile image = mock(MultipartFile.class);
        String token = "invalidToken"; // Assuming an invalid token
        String authorizationHeader = "Bearer " + token;
        String orgName = "original";
        when(permissions.checkToken(authorizationHeader)).thenReturn(false);

        // Act
        ResponseEntity<String> result = categoryProcessor.editCategory(jsonString, image, authorizationHeader, orgName);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Invalid token", result.getBody());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, never()).checkAdmin(token);
        verify(categoryService, never()).editCategory(any(), any(), any());
    }
    //----------------------------------------------------------------------------------------------------------

    // Error handling test for edit Category
    @Test
    void editCategory_ErrorEditingCategoryTest() throws Exception {
        // Arrange
        String jsonString = "{\"name\": \"Updated Category\"}";
        MultipartFile image = mock(MultipartFile.class);
        when(image.getBytes()).thenReturn(new byte[0]);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        String orgName = "original";
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);
        when(permissions.checkAdmin(token)).thenReturn(true);
        doThrow(new RuntimeException("Error editing category")).when(categoryService).editCategory(any(), any(), any());

        // Act
        ResponseEntity<String> result = categoryProcessor.editCategory(jsonString, image, authorizationHeader, orgName);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("Error editing category", result.getBody());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(categoryService, times(1)).editCategory(any(), any(), any());
    }
    //-------------------------------------------------------------------------------------------------------------

    //Test for Unauthorized customer access for editing a category
    @Test
    void editCategory_UnauthorizedCustomerAccessTest() throws Exception {
        // Arrange
        String jsonString = "{\"name\": \"Updated Category\"}";
        MultipartFile image = mock(MultipartFile.class);
        String token = "validToken";
        String authorizationHeader = "Bearer " + token;
        String orgName = "original";
        // Mocking that the token is valid
        when(permissions.checkToken(authorizationHeader)).thenReturn(true);

        // Mocking that the user is not an admin
        when(permissions.checkAdmin(token)).thenReturn(false);

        // Act
        ResponseEntity<String> result = categoryProcessor.editCategory(jsonString, image, authorizationHeader, orgName);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertEquals("Unauthorized access", result.getBody());

        // Verify
        verify(permissions, times(1)).checkToken(anyString());
        verify(permissions, times(1)).checkAdmin(token);
        verify(categoryService, never()).editCategory(any(), any(), any());
    }
}
