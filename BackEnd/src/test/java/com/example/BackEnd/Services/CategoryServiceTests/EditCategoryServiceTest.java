package com.example.BackEnd.Services.CategoryServiceTests;


import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Services.CategoryService;
import com.example.BackEnd.Services.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private ImageService imageService;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService editCategoryService;

    @Test
    void testEditCategoryPart() throws IOException {
        // Arrange
        String originalName = "OriginalCategory";
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("NewCategory");
        MultipartFile image = mock(MultipartFile.class);

        // Mock the case where the category with the new name doesn't exist
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.empty());

        // Mock the existing category
        Category existingCategory = new Category();
        existingCategory.setCategoryName(originalName);
        existingCategory.setImageLink("OriginalImageLink");
        when(categoryRepository.findByCategoryName(originalName)).thenReturn(Optional.of(existingCategory));

        // Mock image service behavior
        when(imageService.saveImage(any(), any(), anyBoolean())).thenReturn("newImageLink");

        // Act
        assertDoesNotThrow(() -> editCategoryService.editCategory(categoryDTO, image, originalName));

        // Assert
        Mockito.verify(categoryRepository).findByCategoryName(eq(originalName));
        Mockito.verify(imageService).saveImage(eq(image), eq("NewCategory"), eq(true));
        Mockito.verify(imageService).deleteImage(eq("OriginalImageLink"));
        Mockito.verify(categoryRepository).delete(eq(existingCategory));
        Mockito.verify(categoryRepository).save(any(Category.class));
    }
    @Test
    void testEditCategoryImageNotEmptyAndSameName() throws IOException {
        // Arrange
        String originalName = "OriginalCategory";
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("OriginalCategory");  // Same name as originalName
        MultipartFile image = mock(MultipartFile.class);

        // Mock the case where the category with the new name doesn't exist
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.empty());

        // Mock the existing category
        Category existingCategory = new Category();
        existingCategory.setCategoryName(originalName);
        existingCategory.setImageLink("OriginalImageLink");
        when(categoryRepository.findByCategoryName(originalName)).thenReturn(Optional.of(existingCategory));

        // Mock image service behavior
        String newImageLink = "newImageLink";
        when(imageService.saveImage(any(), any(), anyBoolean())).thenReturn(newImageLink);

        // Act
        assertDoesNotThrow(() -> editCategoryService.editCategory(categoryDTO, image, originalName));

        // Assert
        Mockito.verify(imageService).saveImage(eq(image), eq(originalName), eq(true));
        Mockito.verify(imageService).deleteImage(eq("OriginalImageLink"));
        assertEquals(newImageLink, existingCategory.getImageLink());
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        List<Category> mockCategoryList = new ArrayList<>();
        mockCategoryList.add(new Category("Category1", "image-link-1"));
        mockCategoryList.add(new Category("Category2", "image-link-2"));

        when(categoryRepository.findAll()).thenReturn(mockCategoryList);

        // Act
        List<CategoryDTO> result = editCategoryService.getAllCategories();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0).getName());
        assertEquals("image-link-1", result.get(0).getImageUrl());
        assertEquals("Category2", result.get(1).getName());
        assertEquals("image-link-2", result.get(1).getImageUrl());
    }
    @Test
    void getAllCategories_ReturnsListOfCategoryDTO() {
        // Arrange
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("Category1", "image-link1"));
        categoryList.add(new Category("Category2", "image-link2"));

        when(categoryRepository.findAll()).thenReturn(categoryList);

        // Act
        List<CategoryDTO> result = editCategoryService.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(categoryList.size(), result.size());
        // Add more specific assertions based on your actual implementation
    }
    @Test
    void getCategory_NonExistingCategory_ReturnsNull() {
        // Arrange
        String categoryName = "NonExistingCategory";
        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.empty());

        // Act
        CategoryDTO result = editCategoryService.getCategory(categoryName);

        // Assert
        assertNull(result);
    }


}
