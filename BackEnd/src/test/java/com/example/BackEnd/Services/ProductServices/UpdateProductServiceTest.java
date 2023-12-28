package com.example.BackEnd.Services.ProductServices;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Services.ImageService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UpdateProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ImageService imageService;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private UpdateProductService updateProductService;

    @Test
    public void testSuccessMessage() {
        assertEquals("Product updated successfully", updateProductService.getSuccessMessage());
    }

    @Test
    @Transactional
    void updateProductHappyCase() throws IOException {
        // Arrange
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .productName("test product")
                .price(100)
                .postedDate(LocalDateTime.now())
                .description("test description")
                .productCountAvailable(10)
                .brand("test brand")
                .category("test category")
                .build();

        MultipartFile image = mock(MultipartFile.class);
        Category category = new Category(); // initialize a category
        category.setCategoryName("test category");
        category.setImageLink("/path/to/category/image");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        when(productRepository.save(any())).thenReturn(new Product());
        when(imageService.saveImage(any(), any(), eq(false))).thenReturn("/path/to/image");

        // Act
        updateProductService.processProduct(productDTO, image);

        // Assert
        verify(productRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(1)).save(any());
        verify(imageService, times(1)).saveImage(any(), any(),eq(false));
    }

    @Test
    @Transactional
    void updateProductCategoryDoesNotExist() throws IOException {
        // Arrange
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .productName("test product")
                .price(100)
                .postedDate(LocalDateTime.now())
                .description("test description")
                .productCountAvailable(10)
                .brand("test brand")
                .category("test category")
                .build();

        MultipartFile image = mock(MultipartFile.class);
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));

        // Act, Assert
        assertThrows(NoSuchElementException.class, () -> updateProductService.processProduct(productDTO, image));
        verify(productRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(0)).save(any());
        verify(imageService, times(0)).saveImage(any(), any(),eq(false));
    }

    @Test
    @Transactional
    void updateProductProductDoesNotExist() throws IOException {
        // Arrange
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .productName("test product")
                .price(100)
                .postedDate(LocalDateTime.now())
                .description("test description")
                .productCountAvailable(10)
                .brand("test brand")
                .category("test category")
                .build();

        MultipartFile image = mock(MultipartFile.class);

        when(productRepository.findById(any())).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(NoSuchElementException.class, () -> updateProductService.processProduct(productDTO, image));
        verify(productRepository, times(1)).findById(any());
        verify(categoryRepository, times(0)).findById(any());
        verify(productRepository, times(0)).save(any());
        verify(imageService, times(0)).saveImage(any(), any(),eq(false));
    }

    @Test
    @Transactional
    void addProductIOException() throws IOException {
        // Arrange
        ProductDTO productDTO = ProductDTO.builder()
                .productName("test product")
                .price(100)
                .postedDate(LocalDateTime.now())
                .description("test description")
                .productCountAvailable(10)
                .brand("test brand")
                .category("test category")
                .build();

        MultipartFile image = mock(MultipartFile.class);
        Category category = new Category(); // initialize a category
        category.setCategoryName("test category");
        category.setImageLink("/path/to/category/image");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        when(imageService.saveImage(any(), any(),eq(false))).thenThrow(new IOException());

        // Act, Assert
        assertThrows(IOException.class, () -> updateProductService.processProduct(productDTO, image));
        verify(productRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(0)).save(any());
        verify(imageService, times(1)).saveImage(any(), any(),eq(false));
    }

    @Test
    @Transactional
    void addProductIllegalStateException() throws IOException {
        // Arrange
        ProductDTO productDTO = ProductDTO.builder()
                .productName("test product")
                .price(100)
                .postedDate(LocalDateTime.now())
                .description("test description")
                .productCountAvailable(10)
                .brand("test brand")
                .category("test category")
                .build();

        MultipartFile image = mock(MultipartFile.class);
        Category category = new Category(); // initialize a category
        category.setCategoryName("test category");
        category.setImageLink("/path/to/category/image");

        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(productRepository.findById(any())).thenReturn(Optional.of(new Product()));
        when(imageService.saveImage(any(), any(),eq(false))).thenThrow(new IllegalStateException());

        // Act, Assert
        assertThrows(IllegalStateException.class, () -> updateProductService.processProduct(productDTO, image));
        verify(productRepository, times(1)).findById(any());
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(0)).save(any());
        verify(imageService, times(1)).saveImage(any(), any(),eq(false));
    }
}