package com.example.BackEnd.Services;

import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.WishList;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.example.BackEnd.Repositories.WishListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerCatalogServiceTest {
    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private CustomerCartRepository customerCartRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CustomerCatalogService customerCatalogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }

    @Test
    void getProducts_ReturnsListOfCustomerProducts() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        products.add(product);
        when(productRepository.findAll()).thenReturn(products);

        Long customerId = 1L, productId = 1L;
        when(wishListRepository.findByCustomer_IdAndProduct_Id(customerId, productId))
                .thenReturn(Optional.of(new WishList()));
        when(customerCartRepository.findByCustomer_IdAndProduct_Id(customerId, productId))
                .thenReturn(Optional.of(new CustomerCart()));

        List<CustomerProduct> result = customerCatalogService.getProducts(customerId);

        verify(productRepository).findAll();
        verify(wishListRepository, atLeastOnce()).findByCustomer_IdAndProduct_Id(customerId, productId);
        verify(customerCartRepository, atLeastOnce()).findByCustomer_IdAndProduct_Id(customerId, productId);
        assertEquals(products.size(), result.size());
    }

    @Test
    void getProducts_ProductRepositoryThrowsException_ReturnsEmptyList() {
        Long customerId = 1L;

        when(productRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        List<CustomerProduct> result = customerCatalogService.getProducts(customerId);

        verify(productRepository).findAll();
        assertEquals(0, result.size()); // Expect an empty list due to exception
    }

    @Test
    void getProducts_WishListRepositoryThrowsException_ReturnsEmptyList() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        products.add(product);
        when(productRepository.findAll()).thenReturn(products);

        Long customerId = 1L, productId = 1L;
        when(wishListRepository.findByCustomer_IdAndProduct_Id(customerId, productId))
                .thenThrow(new RuntimeException("Database error"));

        List<CustomerProduct> result = customerCatalogService.getProducts(customerId);

        verify(productRepository).findAll();
        verify(wishListRepository).findByCustomer_IdAndProduct_Id(customerId, productId);
        assertEquals(0, result.size()); // Expect an empty list due to exception
    }

    @Test
    void getProducts_CustomerCartRepositoryThrowsException_ReturnsEmptyList() {
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setId(1L);
        products.add(product);
        when(productRepository.findAll()).thenReturn(products);

        Long customerId = 1L, productId = 1L;
        when(wishListRepository.findByCustomer_IdAndProduct_Id(customerId, productId))
                .thenReturn(Optional.of(new WishList()));
        when(customerCartRepository.findByCustomer_IdAndProduct_Id(customerId, productId))
                .thenThrow(new RuntimeException("Database error"));

        List<CustomerProduct> result = customerCatalogService.getProducts(customerId);

        verify(productRepository).findAll();
        verify(wishListRepository).findByCustomer_IdAndProduct_Id(customerId, productId);
        verify(customerCartRepository).findByCustomer_IdAndProduct_Id(customerId, productId);
        assertEquals(0, result.size()); // Expect an empty list due to exception
    }

}
