package com.example.BackEnd.Services;

import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerOrdersServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CustomerOrdersService customerOrdersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }

    @Test
    void retrieveOrders_ReturnsListOfOrders() {
        Customer customer = new Customer();
        when(orderRepository.findByCustomer(customer)).thenReturn(new ArrayList<>());

        List<Order> result = customerOrdersService.retrieveOrders(customer);

        verify(orderRepository).findByCustomer(customer);
        assertEquals(0, result.size()); // Assuming an empty list is returned
    }
}
