package com.example.BackEnd.Services;

import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.OrderItemRepository;
import com.example.BackEnd.Repositories.OrderRepository;
import com.example.BackEnd.Repositories.ProductRepository;
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

class DashboardServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
    }

    @Test
    void retrieveOrders_ReturnsListOfOrders() {
        when(orderRepository.findAll()).thenReturn(new ArrayList<>());

        List<Order> result = dashboardService.retrieveOrders();

        verify(orderRepository).findAll();
        assertEquals(0, result.size()); // Assuming an empty list is returned
    }

    @Test
    void cancelOrder_ExistingOrder_DeletesOrder() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = dashboardService.cancelOrder(1L);

        verify(orderRepository).delete(order);
        assertEquals("Order deleted", result);
    }

    @Test
    void cancelOrder_NonExistingOrder_ReturnsNotFoundMessage() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        String result = dashboardService.cancelOrder(1L);

        assertEquals("Order not found, please refresh the page and try again. If the problem persists, please contact one of the development team members.", result);
    }

    @Test
    void updateOrderStatus_ExistingOrder_UpdatesStatus() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        String result = dashboardService.updateOrderStatus(1L, "Updated Status");

        verify(orderRepository).save(order);
        assertEquals("Updated Status", order.getStatus());
        assertEquals("Status updated", result);
    }

    @Test
    void updateOrderStatus_NonExistingOrder_ReturnsNotFoundMessage() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        String result = dashboardService.updateOrderStatus(1L, "Updated Status");

        assertEquals("Order not found, please refresh the page and try again. If the problem persists, please contact one of the development team members.", result);
    }

    @Test
    void deleteOrderItem_ExistingOrderAndProduct_DeletesItem() {
        Order order = new Order();
        order.setId(1L);
        Product product = new Product();
        product.setId(1L);
        OrderItem orderItem = new OrderItem(order, product, 1, 1);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.findById(new OrderItemPK(order, product))).thenReturn(Optional.of(orderItem));

        String result = dashboardService.deleteOrderItem(1L, 1L);

        verify(orderItemRepository).delete(orderItem);
        assertEquals("Item deleted", result);
    }

    @Test
    void deleteOrderItem_NonExistingOrderOrProduct_ReturnsNotFoundMessage() {
        // Assuming both Order and Product do not exist
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        String result = dashboardService.deleteOrderItem(1L, 1L);

        assertEquals("Both the order and the product associated with the item are missing from the database," +
                " please contact one of the development team members.", result);
    }

    @Test
    void deleteOrderItem_NonExistingOrderAndExistingProduct_ReturnsNotFoundMessage() {
        // Assuming Order does not exist
        Product product = new Product();
        product.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = dashboardService.deleteOrderItem(1L, 1L);

        assertEquals("The order associated with the item is missing from the database," +
                " please contact one of the development team members.", result);
    }

    @Test
    void deleteOrderItem_ExistingOrderAndNonExistingProduct_ReturnsNotFoundMessage() {
        // Assuming Product does not exist
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        String result = dashboardService.deleteOrderItem(1L, 1L);

        assertEquals("The product associated with the item is missing from the database," +
                " please contact one of the development team members.", result);
    }

    @Test
    void deleteOrderItem_ExistingOrderAndProductAndNonExistingItem_ReturnsNotFoundMessage() {
        Order order = new Order();
        order.setId(1L);
        Product product = new Product();
        product.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderItemRepository.findById(new OrderItemPK(order, product))).thenReturn(Optional.empty());

        String result = dashboardService.deleteOrderItem(1L, 1L);

        assertEquals("Item not found, please refresh the page and try again. If the problem persists," +
                " please contact one of the development team members.", result);
    }
}