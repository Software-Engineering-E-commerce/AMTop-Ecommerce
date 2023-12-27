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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class EditCategoryServiceTest {
    @Mock
    private ImageService imageService;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryService categoryService;
    //-------------------------------------------------------
    //Test for success message
    @Test
    public void getSuccessMessageTest(){
        assertEquals("Category edited successfully", categoryService.getSuccessMessage(false));
    }
    //--------------------------------------------------------------------
    //Test for edit category successfully
    @Test
    @Transactional
    void editCategoryHappyCase() throws IOException{
        //Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("test category")
                .build();
        MultipartFile image = mock(MultipartFile.class);
        when(categoryRepository.findByCategoryName(any())).thenReturn(Optional.of(new Category()));
        when(categoryRepository.save(any())).thenReturn(new Category());
        when(imageService.saveImage(any(), any(), eq(true))).thenReturn("/path/to/image");
        //Act
        categoryService.editCategory(categoryDTO,image);
        //Assert
        verify(categoryRepository, times(1)).findByCategoryName(any());
        verify(categoryRepository, times(1)).save(any());
        verify(imageService, times(1)).saveImage(any(), any(),eq(true));
    }
    //-------------------------------------------------------------
    //Test for editing in existent category
    @Test
    @Transactional
    void editCategoryDoesNotExist() throws IOException {
        //Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .name("test category")
                .build();
        MultipartFile image = mock(MultipartFile.class);
        when(categoryRepository.findByCategoryName(any())).thenReturn(Optional.empty());
        //Act
        assertThrows(NoSuchElementException.class, () -> categoryService.editCategory(categoryDTO, image));
        //Assert
        verify(categoryRepository, times(1)).findByCategoryName(any());
        verify(categoryRepository, times(0)).save(any());
        verify(imageService, times(0)).saveImage(any(), any(),eq(true));
    }
    //-------------------------------------------------------
    //Test for editing category io exception
    @Test
    @Transactional
    void editCategoryIOException() throws IOException {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();

        MultipartFile image = mock(MultipartFile.class);

        Category category = new Category();
        category.setCategoryName("existed category");
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByCategoryName(any())).thenReturn(Optional.of(category));
        when(imageService.saveImage(any(), any(), eq(true))).thenThrow(new IOException());

        // Act, Assert
        assertThrows(IOException.class, () -> categoryService.editCategory(categoryDTO, image));
        verify(categoryRepository, times(1)).findByCategoryName(any());
        verify(imageService, times(1)).saveImage(any(), any(), eq(true));
    }

    //-------------------------------------------------------
    //Test for editing category illegal state exception
    @Test
    @Transactional
    void editCategoryIllegalStateException() throws IOException {
        // Initiating
        CategoryDTO categoryDTO = CategoryDTO.builder().name("test category").build();

        MultipartFile image = mock(MultipartFile.class);
        Category category = new Category();
        category.setCategoryName("existed category");
        category.setImageLink("/path/to/category/image");
        when(categoryRepository.findByCategoryName(any())).thenReturn(Optional.of(category));
        when(imageService.saveImage(any(), any(), eq(true))).thenThrow(new IllegalStateException());

        // Act, Assert
        assertThrows(IllegalStateException.class, () -> categoryService.editCategory(categoryDTO, image));
        verify(categoryRepository, times(1)).findByCategoryName(any());
        verify(imageService, times(1)).saveImage(any(), any(), eq(true));
    }
}
