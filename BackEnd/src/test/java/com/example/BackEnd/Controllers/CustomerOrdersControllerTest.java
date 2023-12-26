package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Services.CustomerOrdersService;
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
import static org.mockito.Mockito.*;


class CustomerOrdersControllerTest {

    @Mock
    private CustomerOrdersService customerOrdersService;

    @Mock
    private JwtService jwtService;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerOrdersController customerOrdersController;

    private final String customerToken = "Bearer customerToken";
    private final String nonCustomerToken = "Bearer nonCustomerToken";
    private final String invalidToken = "Invalid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
        Customer customer = new Customer();
        String customerEmail = "customer@example.com";
        customer.setEmail(customerEmail);

        when(jwtService.extractUsername(customerToken.substring(7))).thenReturn(customerEmail);
        when(customerRepository.findByEmail(customerEmail)).thenReturn(Optional.of(customer));

        String nonCustomerEmail = "user@example.com";
        when(jwtService.extractUsername(nonCustomerToken.substring(7))).thenReturn(nonCustomerEmail);
        when(customerRepository.findByEmail(nonCustomerEmail)).thenReturn(Optional.empty());
    }

    @Test
    void whenGetOrdersAsCustomer_thenReturnCustomerOrdersList() {
        when(customerOrdersService.retrieveOrders(any())).thenReturn(new ArrayList<>());

        ResponseEntity<List<Order>> response = customerOrdersController.getOrders(customerToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(customerOrdersService).retrieveOrders(any());
    }

    @Test
    void whenGetOrdersAsNonCustomer_thenForbidden() {
        ResponseEntity<List<Order>> response = customerOrdersController.getOrders(nonCustomerToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Invalid tokens tests
    @Test
    void whenGetOrdersWithInvalidToken_thenThrowIllegalArgumentException() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            customerOrdersController.getOrders(invalidToken);
        });

        assertNotNull(thrownException.getMessage());
    }
}
