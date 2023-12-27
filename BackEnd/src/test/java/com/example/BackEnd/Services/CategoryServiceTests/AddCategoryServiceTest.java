package com.example.BackEnd.Services.CategoryServiceTests;

import com.example.BackEnd.DTO.CategoryDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Services.CategoryService;
import com.example.BackEnd.Services.ImageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class AddCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private CategoryService categoryService;

    //-------------------------------------------------------
    //Test for success message
    @Test
    public void getSuccessMessageTest() {
        assertEquals("Category added successfully", categoryService.getSuccessMessage(true));
    }

    //-------------------------------------------------------
    //Test for adding category successfully
    @Test
    @Transactional
    void addCategoryHappyCase() throws Exception {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();
        MultipartFile image = mock(MultipartFile.class);
        when(categoryRepository.save(any())).thenReturn(new Category());
        when(imageService.saveImage(any(), any(), eq(true))).thenReturn("/path/to/image");

        // Act
        categoryService.addCategory(categoryDTO, image);

        // Assert
        verify(categoryRepository, times(1)).findByName(any());
        verify(imageService, times(1)).saveImage(any(), any(), eq(true));
    }

    //-------------------------------------------------------
    //Test for adding existing category
    @Test
    @Transactional
    void addCategoryAlreadyExist() throws IOException {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();
        MultipartFile image = mock(MultipartFile.class);
        Category category = new Category(); // initialize a category
        category.setCategoryName("test category");
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByName(any())).thenReturn(Optional.of(category));

        // Act, Assert
        assertThrows(Exception.class, () -> categoryService.addCategory(categoryDTO, image));
        verify(categoryRepository, times(1)).findByName(any());
        verify(imageService, times(0)).saveImage(any(), any(), eq(true));
    }

    //-------------------------------------------------------
    //Test for adding category io exception
    @Test
    @Transactional
    void addCategoryIOException() throws IOException {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();

        MultipartFile image = mock(MultipartFile.class);

        when(categoryRepository.save(any())).thenReturn(new Category());
        when(imageService.saveImage(any(), any(), eq(true))).thenThrow(new IOException());

        // Act, Assert
        assertThrows(IOException.class, () -> categoryService.addCategory(categoryDTO, image));
        verify(categoryRepository, times(1)).findByName(any());
        verify(imageService, times(1)).saveImage(any(), any(), eq(true));
    }

    //-------------------------------------------------------
    //Test for adding category illegal state exception
    @Test
    @Transactional
    void addCategoryIllegalStateException() throws IOException {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();

        MultipartFile image = mock(MultipartFile.class);

        when(categoryRepository.save(any())).thenReturn(new Category());
        when(imageService.saveImage(any(), any(), eq(true))).thenThrow(new IllegalStateException());

        // Act, Assert
        assertThrows(IllegalStateException.class, () -> categoryService.addCategory(categoryDTO, image));
        verify(categoryRepository, times(1)).findByName(any());
        verify(imageService, times(1)).saveImage(any(), any(), eq(true));
    }
}
