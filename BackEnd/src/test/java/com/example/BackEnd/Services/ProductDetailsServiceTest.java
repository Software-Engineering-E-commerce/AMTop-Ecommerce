package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.ProductResponse;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.nimbusds.oauth2.sdk.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDetailsServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private ProductDetailsService productDetailsService;

    @Test
    void exceptionHandlingTest() {
        // Arrange
        Long productID = 1L;
        String token = "AdminToken";
        String email = "admin@gmail.com";
        when(productRepository.findById(productID)).thenThrow(RuntimeException.class);
        // Act
        ProductResponse result = productDetailsService.getProduct(productID, token);

        // Assert
        assertNull(result);
        verify(productRepository, times(1)).findById(productID);
    }


    @Test
    void productNotFoundTest() {
        Long productID = 1L;
        String token = "validToken";
        when(productRepository.findById(productID)).thenReturn(Optional.empty());
        assertNull(productDetailsService.getProduct(productID, token));
        verify(productRepository, times(1)).findById(productID);
    }

    @Test
    void productWithoutReviewsTest() {
        Long productID = 1L;
        String token = "AdminToken";
        String email = "admin@gmail.com";
        Product product = createProduct();
        when(productRepository.findById(productID)).thenReturn(Optional.of(product));
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(new Admin()));
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        ProductResponse productResponse = productDetailsService.getProduct(productID, token);
        assertNotNull(productResponse);
        assertTrue(productResponse.isAdmin());
        assertEquals(productResponse.getReviews().size(), 0);
        assertEquals(productResponse.getId(), 1);
        assertEquals(productResponse.getName(), "Dummy Product");
        assertEquals(productResponse.getDiscountPercentage(), 10.0f);
        assertEquals(productResponse.getBrand(), "Dummy Brand");
        assertFalse(productResponse.isInWishlist());
    }


    @Test
    void productForCustomerTest() {
        Long productID = 1L;
        String token = "CustomerToken";
        String email = "customer@gmail.com";
        Product product = createProduct();
        when(productRepository.findById(productID)).thenReturn(Optional.of(product));
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(new Customer()));
        ProductResponse productResponse = productDetailsService.getProduct(productID, token);
        assertNotNull(productResponse);
        assertFalse(productResponse.isAdmin());
        assertEquals(productResponse.getReviews().size(), 0);
        assertEquals(productResponse.getId(), 1);
        assertEquals(productResponse.getName(), "Dummy Product");
        assertEquals(productResponse.getDiscountPercentage(), 10.0f);
        assertEquals(productResponse.getBrand(), "Dummy Brand");
        assertFalse(productResponse.isInWishlist());
    }

    @Test
    void productWithReviewsTest() {
        Long productID = 1L;
        String token = "AdminToken";
        String email = "admin@gmail.com";
        Product product = createProductWithReview();
        when(productRepository.findById(productID)).thenReturn(Optional.of(product));
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(new Admin()));
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        ProductResponse productResponse = productDetailsService.getProduct(productID, token);
        assertNotNull(productResponse);
        assertTrue(productResponse.isAdmin());
        assertEquals(productResponse.getId(), 1);
        assertEquals(productResponse.getName(), "Dummy Product");
        assertEquals(productResponse.getDiscountPercentage(), 10.0f);
        assertEquals(productResponse.getBrand(), "Dummy Brand");
        assertEquals(productResponse.getReviews().size(), 2);
        assertEquals(productResponse.getReviews().get(0).getRating(), 4.5f);
        assertEquals(productResponse.getReviews().get(1).getRating(), 3.5f);
        assertFalse(productResponse.isInWishlist());
    }

    @Test
    void productWithReviewsCustomerTest() {
        Long productID = 1L;
        String token = "CustomerToken";
        String email = "customer@gmail.com";
        Product product = createProductWithReview();
        when(productRepository.findById(productID)).thenReturn(Optional.of(product));
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(new Customer()));
        ProductResponse productResponse = productDetailsService.getProduct(productID, token);
        assertNotNull(productResponse);
        assertFalse(productResponse.isAdmin());
        assertEquals(productResponse.getId(), 1);
        assertEquals(productResponse.getName(), "Dummy Product");
        assertEquals(productResponse.getDiscountPercentage(), 10.0f);
        assertEquals(productResponse.getBrand(), "Dummy Brand");
        assertEquals(productResponse.getReviews().size(), 2);
        assertEquals(productResponse.getReviews().get(0).getRating(), 4.5f);
        assertEquals(productResponse.getReviews().get(1).getRating(), 3.5f);
        assertFalse(productResponse.isInWishlist());
    }

    private Product createProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setProductName("Dummy Product");
        product.setPrice(50.0f);
        product.setPostedDate(LocalDateTime.now());
        product.setDescription("This is a dummy product for testing");
        product.setProductCountAvailable(100);
        product.setProductSoldCount(0);
        product.setBrand("Dummy Brand");
        product.setImageLink("dummy-image-link.jpg");
        product.setDiscountPercentage(10.0f);

        // Create a dummy category
        Category category = new Category();
        category.setCategoryName("Dummy Category");
        category.setImageLink("dummy-category-image.jpg");

        // Set the category for the product
        product.setCategory(category);

        return product;
    }

    private Product createProductWithReview() {
        Product product = new Product();
        product.setId(1L);
        product.setProductName("Dummy Product");
        product.setPrice(50.0f);
        product.setPostedDate(LocalDateTime.now());
        product.setDescription("This is a dummy product for testing");
        product.setProductCountAvailable(100);
        product.setProductSoldCount(0);
        product.setBrand("Dummy Brand");
        product.setImageLink("dummy-image-link.jpg");
        product.setDiscountPercentage(10.0f);

        // Create a dummy category
        Category category = new Category();
        category.setCategoryName("Dummy Category");
        category.setImageLink("dummy-category-image.jpg");

        // Set the category for the product
        product.setCategory(category);

        // Create a dummy review
        Review review = new Review();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("dummy@example.com");
        customer.setPassword("dummyPassword");
        customer.setIsGmail(false);
        customer.setIsVerified(true);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(4.5f);
        review.setComment("Great product!");
        review.setDate(LocalDateTime.now());

        // Add the dummy review to the product's reviews list
        product.getReviews().add(review);

        Customer customer2 = new Customer();
        customer.setId(2L);
        customer.setEmail("dummy@example.com");
        customer.setPassword("dummyPassword");
        customer.setIsGmail(false);
        customer.setIsVerified(true);
        customer.setFirstName("John");
        customer.setLastName("Doe");

        Review review2 = new Review(customer2, product, 3.5f, "good!", LocalDateTime.now());

        product.getReviews().add(review2);

        return product;
    }

}