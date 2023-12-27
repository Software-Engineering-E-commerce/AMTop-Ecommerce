package com.example.BackEnd.Services.CategoryServiceTests;

import com.example.BackEnd.DTO.CategoryResponse;
import com.example.BackEnd.Middleware.Permissions;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Services.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private Permissions permissions;

    @Test
    void exceptionHandlingTest() {
        String name = "test";
        String token = "AdminToken";
        when(categoryRepository.findByCategoryName(name)).thenThrow(RuntimeException.class);
        //Act
        CategoryResponse result = categoryService.getCategory(name, token);

        //Assert
        assertNull(result);
        verify(categoryRepository, times(1)).findByCategoryName(name);
    }

    @Test
    void categoryNotFoundTest() {
        String catName = "test";
        String token = "validToken";
        when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.empty());
        assertNull(categoryService.getCategory(catName, token));
        verify(categoryRepository, times(1)).findByCategoryName(catName);
    }

    @Test
    void categoryForCustomerTest() {
        String catName = "test category";
        String token = "CustomerToken";
        Category category = new Category();
        category.setCategoryName(catName);
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.of(category));
        when(permissions.checkAdmin(token)).thenReturn(false);
        CategoryResponse categoryResponse = categoryService.getCategory(catName, token);
        assertNotNull(categoryResponse);
        assertFalse(categoryResponse.isAdmin());
        assertEquals(categoryResponse.getName(), "test category");
    }

    @Test
    void categoryForAdminTest() {
        String catName = "test category";
        String token = "AdminToken";
        Category category = new Category();
        category.setCategoryName(catName);
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.of(category));
        when(permissions.checkAdmin(token)).thenReturn(true);
        CategoryResponse categoryResponse = categoryService.getCategory(catName, token);
        assertNotNull(categoryResponse);
        assertTrue(categoryResponse.isAdmin());
        assertEquals(categoryResponse.getName(), "test category");
    }

}
