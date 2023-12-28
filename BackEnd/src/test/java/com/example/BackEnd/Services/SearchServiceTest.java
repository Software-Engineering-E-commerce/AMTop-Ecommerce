package com.example.BackEnd.Services;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Services.FilterService.FilterProducts;
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
class SearchServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SearchService searchService;

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
    Product product11;
    Product product12;
    Product product13;

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

        product11 = objectMapper.readValue(new File(
                "src/test/resources/products/product11.json"), Product.class);
        productRepository.save(product11);

        product12 = objectMapper.readValue(new File(
                "src/test/resources/products/product12.json"), Product.class);
        productRepository.save(product12);

        product13 = objectMapper.readValue(new File(
                "src/test/resources/products/product13.json"), Product.class);
        productRepository.save(product13);
    }

    @Test
    void testSearchByProductName() {
        List<Product> result = searchService.search("Product1");

        //product1, 10
        assertEquals(5, result.size());
        List<Product> expected = List.of(product1, product10, product11, product12, product13);
        assertEquals(expected, result);
    }

    @Test
    void testProductComp() {
        List<Product> result = searchService.search("del");
        assertEquals(2, result.size());
        List<Product> expected = List.of(product11, product12);
        assertEquals(expected, result);
    }

    @Test
    void testSearchByDescription() {
        List<Product> result = searchService.search("ription5");

        assertEquals(1, result.size());
        assertEquals("Product5", result.get(0).getProductName());
    }

    @Test
    void testSearchByBrand() {
        List<Product> result = searchService.search("Brand6");

        assertEquals(3, result.size());
        List<Product> expected = List.of(product6, product7, product8);
        assertEquals(expected, result);
    }

    @Test
    void testSearchByCategory() {
        List<Product> result = searchService.search("tegory8");

        assertEquals(6, result.size());
        List<Product> expected = List.of(product8, product9, product10, product11, product12, product13);
        assertEquals(expected, result);
    }

    @Test
    void testSearchWithNoMatch() {
        List<Product> result = searchService.search("NonExistentProduct");

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchByPartialProductName() {
        List<Product> result = searchService.search("Product");

        assertEquals(13, result.size()); // Expecting all products as they all contain "Product"
    }

    @Test
    void testSearchByCaseInsensitiveKey() {
        List<Product> result = searchService.search("PRODUCT3");

        assertEquals(1, result.size()); // Expecting a match regardless of case sensitivity
    }

    @Test
    void testSearchByEmptyString() {
        List<Product> result = searchService.search("");

        assertEquals(13, result.size()); // Expecting all products when the key is an empty string
    }

}