package com.example.BackEnd.Services.CategoryServiceTests;

import com.example.BackEnd.DTO.CategoryDTO;
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
        when(categoryRepository.findByCategoryName(name)).thenThrow(RuntimeException.class);
        //Act
        CategoryDTO result = categoryService.getCategory(name);

        //Assert
        assertNull(result);
        verify(categoryRepository, times(1)).findByCategoryName(name);
    }

    @Test
    void categoryNotFoundTest() {
        String catName = "test";
        when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.empty());
        assertNull(categoryService.getCategory(catName));
        verify(categoryRepository, times(1)).findByCategoryName(catName);
    }

    @Test
    void categorySuccessTest1() {
        String catName = "test category";
        Category category = new Category();
        category.setCategoryName(catName);
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.of(category));
        CategoryDTO categoryDTO = categoryService.getCategory(catName);
        assertNotNull(categoryDTO);
        assertEquals(categoryDTO.getName(), "test category");
    }

    @Test
    void categorySuccessTest2() {
        String catName = "test category";
        Category category = new Category();
        category.setCategoryName(catName);
        category.setImageLink("/path/to/category/image");
        lenient().when(categoryRepository.findByCategoryName(catName)).thenReturn(Optional.of(category));
        CategoryDTO categoryDTO = categoryService.getCategory(catName);
        assertNotNull(categoryDTO);
        assertEquals(categoryDTO.getName(), "test category");
    }

}
