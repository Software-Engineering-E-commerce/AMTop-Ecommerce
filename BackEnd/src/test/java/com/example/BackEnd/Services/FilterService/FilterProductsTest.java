package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.DTO.FilterProductDto;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FilterProductsTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    FilterProducts filterProducts;

    Product product1;
    Product product2;
    Product product3;
    Product product4;
    Product product5;
    Product product6;
    Product product7;
    Product product8;
    Product product9;
    Product product10;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        Category category1 = objectMapper.readValue(new File(
                "src/test/resources/categories/category1.json"), Category.class);
        Category category2 = objectMapper.readValue(new File(
                "src/test/resources/categories/category2.json"), Category.class);
        Category category3 = objectMapper.readValue(new File(
                "src/test/resources/categories/category3.json"), Category.class);
        Category category6 = objectMapper.readValue(new File(
                "src/test/resources/categories/category6.json"), Category.class);
        Category category8 = objectMapper.readValue(new File(
                "src/test/resources/categories/category8.json"), Category.class);

        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);
        categoryRepository.save(category6);
        categoryRepository.save(category8);

        product1 = objectMapper.readValue(new File(
                "src/test/resources/products/product1.json"), Product.class);
        productRepository.save(product1);

        product2 = objectMapper.readValue(new File(
                "src/test/resources/products/product2.json"), Product.class);
        productRepository.save(product2);

        product3 = objectMapper.readValue(new File(
                "src/test/resources/products/product3.json"), Product.class);
        productRepository.save(product3);

        product4 = objectMapper.readValue(new File(
                "src/test/resources/products/product4.json"), Product.class);
        productRepository.save(product4);

        product5 = objectMapper.readValue(new File(
                "src/test/resources/products/product5.json"), Product.class);
        productRepository.save(product5);

        product6 = objectMapper.readValue(new File(
                "src/test/resources/products/product6.json"), Product.class);
        productRepository.save(product6);

        product7 = objectMapper.readValue(new File(
                "src/test/resources/products/product7.json"), Product.class);
        productRepository.save(product7);

        product8 = objectMapper.readValue(new File(
                "src/test/resources/products/product8.json"), Product.class);
        productRepository.save(product8);

        product9 = objectMapper.readValue(new File(
                "src/test/resources/products/product9.json"), Product.class);
        productRepository.save(product9);

        product10 = objectMapper.readValue(new File(
                "src/test/resources/products/product10.json"), Product.class);
        productRepository.save(product10);
    }

    @Test
    void testFilterProducts() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(90)
                .toPrice(100)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(50)
                .productName("Product1")
                .available(true)
                .brand("Brand1")
                .category("Category1")
                .description("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(1, filteredProducts.size());

        // Additional assertions based on your data
        Product product = filteredProducts.get(0);
        assertEquals("Product1", product.getProductName());
        assertTrue(product.getProductCountAvailable() > 0);
        assertEquals("Brand1", product.getBrand());
        assertEquals("Category1", product.getCategory().getCategoryName());

    }

    @Test
    void testFilterComp1() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(20)
                .available(true)
                .brand("Brand6")
                .productName("")
                .description("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(3, filteredProducts.size());

        List<Product> expected = List.of(product6, product7, product8);
        assertEquals(expected, filteredProducts);
    }

    @Test
    void testFilterComp2() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(20)
                .available(true)
                .brand("Brand6")
                .category("Category6")
                .productName("")
                .description("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(2, filteredProducts.size());

        List<Product> expected = List.of(product6, product7);
        assertEquals(expected, filteredProducts);
    }

    @Test
    void testFilterComp3() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(20)
                .available(true)
                .category("Category8")
                .productName("")
                .description("")
                .brand("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(3, filteredProducts.size());

        List<Product> expected = List.of(product8, product9, product10);
        assertEquals(expected, filteredProducts);
    }

    @Test
    void testFilterComp4() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(400)
                .toPrice(500)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .available(true)
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);
        // Assert
        assertEquals(2, filteredProducts.size());

        //product4 is not available
        List<Product> expected = List.of(product5, product10);
        assertEquals(expected, filteredProducts);
    }

    @Test
    void testFilterComp5() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(400)
                .toPrice(500)
                .fromDiscountPercentage(10)
                .toDiscountPercentage(10)
                .available(false)
                .brand("Brand3")
                .productName("")
                .description("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);
        // Assert
        assertEquals(1, filteredProducts.size());

        //product4 is not available
        List<Product> expected = List.of(product4);
        assertEquals(expected, filteredProducts);
    }

    @Test
    void testFilterByPriceRange() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(400)
                .toPrice(900)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(7, filteredProducts.size());
    }

    @Test
    void testFilterByDiscountPercentageRange() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromDiscountPercentage(10)
                .toDiscountPercentage(20)
                .fromPrice(0)
                .toPrice(1000)
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(8, filteredProducts.size());
    }

    @Test
    void testFilterByProductName() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .productName("Product5")
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .fromPrice(0)
                .toPrice(1000)
                .description("")
                .brand("")
                .category("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(1, filteredProducts.size());
    }

    @Test
    void testFilterByAvailability() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .available(true)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .fromPrice(0)
                .toPrice(1000)
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(9, filteredProducts.size());
    }

    @Test
    void testFilterByBrand() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .brand("Brand6")
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .fromPrice(0)
                .toPrice(1000)
                .productName("")
                .description("")
                .category("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(3, filteredProducts.size());
    }

    @Test
    void testFilterByCategory() {
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .category("Category8")
                .fromDiscountPercentage(10)
                .toDiscountPercentage(20)
                .fromPrice(0)
                .toPrice(1000)
                .productName("")
                .description("")
                .brand("")
                .build();

        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        assertEquals(3, filteredProducts.size());
    }

    @Test
    void testFilterByMultipleCriteria() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(100)
                .toPrice(800)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(15)
                .available(true)
                .brand("Brand6")
                .productName("")
                .description("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(2, filteredProducts.size());
    }

    @Test
    void testFilterByProductNameAndCategory() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .productName("Product8")
                .category("Category8")
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .description("")
                .brand("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(1, filteredProducts.size());
    }

    @Test
    void testDescription() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .description("description7")
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .productName("")
                .brand("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(1, filteredProducts.size());
        assertEquals("Product7", filteredProducts.get(0).getProductName());
        assertEquals("Brand6", filteredProducts.get(0).getBrand());
    }

    @Test
    void testFilterWithNoCriteria() {
        // Arrange
        FilterProductDto filterProductDto = FilterProductDto.builder()
                .fromPrice(0)
                .toPrice(1000)
                .fromDiscountPercentage(0)
                .toDiscountPercentage(100)
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        // Act
        List<Product> filteredProducts = filterProducts.filter(filterProductDto);

        // Assert
        assertEquals(10, filteredProducts.size());
    }

    @Test
    void testFilterWithInvalidCriteria() {
        // Arrange: Create an invalid filter criteria
        FilterProductDto invalidFilterDto = FilterProductDto.builder()
                .fromPrice(1000)
                .toPrice(500)  // Invalid range
                .fromDiscountPercentage(20)
                .toDiscountPercentage(10) // Invalid range
                .productName("")
                .description("")
                .brand("")
                .category("")
                .build();

        // Act: Call the filter method with invalid criteria
        List<Product> filteredProducts = filterProducts.filter(invalidFilterDto);

        // Assert: Check if the result is an empty list due to invalid criteria
        assertTrue(filteredProducts.isEmpty());
    }


}