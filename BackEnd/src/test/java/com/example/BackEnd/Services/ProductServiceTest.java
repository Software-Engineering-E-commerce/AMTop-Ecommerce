package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.ProductDTO;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.ProductRepository;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ImageService imageService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @Transactional
    void addProductHappyCase() throws IOException {
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
        when(productRepository.save(any())).thenReturn(new Product());
        when(imageService.saveImage(any(), any())).thenReturn("/path/to/image");

        // Act
        productService.addProduct(productDTO, image);

        // Assert
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(2)).save(any()); // called twice: once before saving the image link and once after
        verify(imageService, times(1)).saveImage(any(), any());
    }

    @Test
    @Transactional
    void addProductCategoryDoesNotExist() throws IOException {
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

        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        // Act, Assert
        assertThrows(NoSuchElementException.class, () -> productService.addProduct(productDTO, image));
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(0)).save(any());
        verify(imageService, times(0)).saveImage(any(), any());
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
        when(productRepository.save(any())).thenReturn(new Product());
        when(imageService.saveImage(any(), any())).thenThrow(new IOException());

        // Act, Assert
        assertThrows(IOException.class, () -> productService.addProduct(productDTO, image));
        verify(categoryRepository, times(1)).findById(any());
        verify(productRepository, times(1)).save(any());
        verify(imageService, times(1)).saveImage(any(), any());
    }
}