package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CustomerProduct;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.CustomerCatalogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerCatalogControllerTest {
    @Mock
    private CustomerCatalogService customerCatalogService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerCatalogController customerCatalogController;

    private final String customerToken = "Bearer customerToken";
    private final String nonCustomerToken = "Bearer nonCustomerToken";
    private final String invalidToken = "Invalid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
        Customer customer = new Customer();
        String customerEmail = "customer@example.com";
        customer.setEmail(customerEmail);
        customer.setId(1L);

        when(jwtService.extractUsername(customerToken.substring(7))).thenReturn(customerEmail);
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));

        String nonCustomerEmail = "user@example.com";
        when(jwtService.extractUsername(nonCustomerToken.substring(7))).thenReturn(nonCustomerEmail);
        when(customerRepository.findByEmail(nonCustomerEmail)).thenReturn(Optional.empty());
    }

    @Test
    void whenGetProductsAsCustomer_thenReturnProductsList() {
        when(customerCatalogService.getProducts(1L)).thenReturn(new ArrayList<>());

        ResponseEntity<List<CustomerProduct>> response = customerCatalogController.getProducts(customerToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerCatalogService).getProducts(1L);
    }

    @Test
    void whenGetProductsAsNonCustomer_thenForbidden() {
        ResponseEntity<List<CustomerProduct>> response = customerCatalogController.getProducts(nonCustomerToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Invalid tokens test
    @Test
    void whenGetProductsWithInvalidToken_thenThrowIllegalArgumentException() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            customerCatalogController.getProducts(invalidToken);
        });

        assertNotNull(thrownException.getMessage());
    }

}
