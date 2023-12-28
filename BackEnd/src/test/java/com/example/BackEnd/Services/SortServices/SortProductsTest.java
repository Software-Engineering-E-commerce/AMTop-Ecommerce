package com.example.BackEnd.Services.SortServices;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.Review;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SortProductsTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    SortProducts sortProducts;

    Product product1;
    Product product2;
    Product product3;

    Customer customer1;
    Customer customer2;
    Customer customer3;
    Customer customer4;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        customer1 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer1.json"), Customer.class);
        customer2 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer2.json"), Customer.class);
        customer3 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer3.json"), Customer.class);
        customer4 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer4.json"), Customer.class);
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

        Category category1 = objectMapper.readValue(new File(
                "src/test/resources/categories/category1.json"), Category.class);
        Category category2 = objectMapper.readValue(new File(
                "src/test/resources/categories/category2.json"), Category.class);
        Category category3 = objectMapper.readValue(new File(
                "src/test/resources/categories/category3.json"), Category.class);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        product1 = objectMapper.readValue(new File(
                "src/test/resources/products/product1.json"), Product.class);
        productRepository.save(product1);

        product2 = objectMapper.readValue(new File(
                "src/test/resources/products/product2.json"), Product.class);
        productRepository.save(product2);

        product3 = objectMapper.readValue(new File(
                "src/test/resources/products/product3.json"), Product.class);
        productRepository.save(product3);


    }

    @Test
    @Transactional
    void testSortByNameAsc() {
        // Arrange
        List<Product> expected = List.of(product1, product2, product3);

        // Act
        List<Product> actual = sortProducts.sort("productName", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByNameDesc() {
        // Arrange
        List<Product> expected = List.of(product3, product2, product1);

        // Act
        List<Product> actual = sortProducts.sort("productName", false);

        // Assert
        assertEquals(expected, actual);
    }


    @Test
    @Transactional
    void testSortByPriceAsc() {
        // Arrange
        List<Product> expected = List.of(product2, product1, product3);

        // Act
        List<Product> actual = sortProducts.sort("price", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByPriceDesc() {
        // Arrange
        List<Product> expected = List.of(product3, product1, product2);

        // Act
        List<Product> actual = sortProducts.sort("price", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByPostDateAsc() {
        // Arrange
        List<Product> expected = List.of(product1, product3, product2);

        // Act
        List<Product> actual = sortProducts.sort("postedDate", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByPostDateDesc() {
        // Arrange
        List<Product> expected = List.of(product2, product3, product1);

        // Act
        List<Product> actual = sortProducts.sort("postedDate", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByProductCountAvailableAsc() {
        // Arrange
        List<Product> expected = List.of(product3, product1, product2);

        // Act
        List<Product> actual = sortProducts.sort("productCountAvailable", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByProductCountAvailableDesc() {
        // Arrange
        List<Product> expected = List.of(product2, product1, product3);

        // Act
        List<Product> actual = sortProducts.sort("productCountAvailable", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByProductSoldCountAsc() {
        // Arrange
        List<Product> expected = List.of(product2, product1, product3);

        // Act
        List<Product> actual = sortProducts.sort("productSoldCount", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByProductSoldCountDesc() {
        // Arrange
        List<Product> expected = List.of(product3, product1, product2);

        // Act
        List<Product> actual = sortProducts.sort("productSoldCount", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByBrandAsc() {
        // Arrange
        List<Product> expected = List.of(product1, product2, product3);

        // Act
        List<Product> actual = sortProducts.sort("brand", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByBrandDesc() {
        // Arrange
        List<Product> expected = List.of(product3, product2, product1);

        // Act
        List<Product> actual = sortProducts.sort("brand", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByDiscountPercentageAsc() {
        // Arrange
        List<Product> expected = List.of(product3, product2, product1);

        // Act
        List<Product> actual = sortProducts.sort("discountPercentage", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByDiscountPercentageDesc() {
        // Arrange
        List<Product> expected = List.of(product1, product2, product3);

        // Act
        List<Product> actual = sortProducts.sort("discountPercentage", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByCategoryNameAsc() {
        // Arrange
        List<Product> expected = List.of(product1, product2, product3);

        // Act
        List<Product> actual = sortProducts.sort("category", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByCategoryNameDesc() {
        // Arrange
        List<Product> expected = List.of(product3, product2, product1);

        // Act
        List<Product> actual = sortProducts.sort("category", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByAverageRatingAsc() throws IOException {
        // 1 --> 4.75
        // 2 --> 4.5
        // 3 --> 4.667

        // Arrange
        setReview();
        List<Product> expected = List.of(product2, product3, product1);

        // Act
        List<Product> actual = sortProducts.sort("averageRating", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByAverageRatingDesc() throws IOException {
        // 1 --> 4.75
        // 2 --> 4.5
        // 3 --> 4.667

        // Arrange
        setReview();
        List<Product> expected = List.of(product1, product3, product2);

        // Act
        List<Product> actual = sortProducts.sort("averageRating", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByReviewCountAsc() throws IOException {
        // Arrange
        setReview();
        List<Product> expected = List.of(product2, product3, product1);

        // Act
        List<Product> actual = sortProducts.sort("numberOfReviews", true);

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @Transactional
    void testSortByReviewCountDesc() throws IOException {
        // Arrange
        setReview();
        List<Product> expected = List.of(product1, product3, product2);

        // Act
        List<Product> actual = sortProducts.sort("numberOfReviews", false);

        // Assert
        assertEquals(expected, actual);
    }

    @Transactional
    void setReview() throws IOException {
        List<Review> reviews1 = new ArrayList<>();
        List<Review> reviews2 = new ArrayList<>();
        List<Review> reviews3 = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        for (int i = 1; i <= 9; i++) {
            Review review = objectMapper.readValue(new File(
                    "src/test/resources/reviews/review" + i + ".json"), Review.class);
            if (i <= 4) {
                review.setProduct(product1);
                reviews1.add(review);
            } else if (i <= 6) {
                review.setProduct(product2);
                reviews2.add(review);
            } else {
                review.setProduct(product3);
                reviews3.add(review);
            }
        }

        reviews1.get(0).setCustomer(customer1);
        reviews1.get(1).setCustomer(customer2);
        reviews1.get(2).setCustomer(customer3);
        reviews1.get(3).setCustomer(customer4);

        reviews2.get(0).setCustomer(customer1);
        reviews2.get(1).setCustomer(customer2);

        reviews3.get(0).setCustomer(customer1);
        reviews3.get(1).setCustomer(customer2);
        reviews3.get(2).setCustomer(customer3);

        product1.setReviews(reviews1);
        product2.setReviews(reviews2);
        product3.setReviews(reviews3);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}