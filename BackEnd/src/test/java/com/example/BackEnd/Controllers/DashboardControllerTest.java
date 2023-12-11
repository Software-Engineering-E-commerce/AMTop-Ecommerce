package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.DeleteItemRequest;
import com.example.BackEnd.DTO.DeleteOrderRequest;
import com.example.BackEnd.DTO.UpdateOrderRequest;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Services.DashboardService;
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


class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private DashboardController dashboardController;

    private final String adminToken = "Bearer adminToken";
    private final String nonAdminToken = "Bearer nonAdminToken";
    private final String invalidToken = "Invalid";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize Mockito annotations
        Admin admin = new Admin();
        String adminEmail = "admin@example.com";
        admin.setEmail(adminEmail);

        when(jwtService.extractUsername(adminToken.substring(7))).thenReturn(adminEmail);
        when(adminRepository.findByEmail(adminEmail)).thenReturn(Optional.of(admin));

        String nonAdminEmail = "user@example.com";
        when(jwtService.extractUsername(nonAdminToken.substring(7))).thenReturn(nonAdminEmail);
        when(adminRepository.findByEmail(nonAdminEmail)).thenReturn(Optional.empty());
    }

    @Test
    void whenGetOrdersAsAdmin_thenReturnOrdersList() {
        when(dashboardService.retrieveOrders()).thenReturn(new ArrayList<>());

        ResponseEntity<List<Order>> response = dashboardController.getOrders(adminToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dashboardService).retrieveOrders();
    }

    @Test
    void whenGetOrdersAsNonAdmin_thenForbidden() {
        ResponseEntity<List<Order>> response = dashboardController.getOrders(nonAdminToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void whenUpdateOrderStatusAsAdmin_thenSuccess() {
        UpdateOrderRequest request = new UpdateOrderRequest(1L, "NEW_STATUS");

        when(dashboardService.updateOrderStatus(1L, "NEW_STATUS")).thenReturn("Order Updated");

        ResponseEntity<String> response = dashboardController.updateOrderStatus(adminToken, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Updated", response.getBody());
    }

    @Test
    void whenUpdateOrderStatusAsNonAdmin_thenForbidden() {
        UpdateOrderRequest request = new UpdateOrderRequest(1L, "NEW_STATUS");

        ResponseEntity<String> response = dashboardController.updateOrderStatus(nonAdminToken, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void whenDeleteOrderAsAdmin_thenSuccess() {
        DeleteOrderRequest request = new DeleteOrderRequest(1L);

        when(dashboardService.cancelOrder(1L)).thenReturn("Order Deleted");

        ResponseEntity<String> response = dashboardController.deleteOrder(adminToken, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Deleted", response.getBody());
    }

    @Test
    void whenDeleteOrderAsNonAdmin_thenForbidden() {
        DeleteOrderRequest request = new DeleteOrderRequest(1L);

        ResponseEntity<String> response = dashboardController.deleteOrder(nonAdminToken, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void whenDeleteItemAsAdmin_thenSuccess() {
        DeleteItemRequest request = new DeleteItemRequest(1L, 1L);

        when(dashboardService.deleteOrderItem(1L, 1L)).thenReturn("Item Deleted");

        ResponseEntity<String> response = dashboardController.deleteItem(adminToken, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Item Deleted", response.getBody());
    }

    @Test
    void whenDeleteItemAsNonAdmin_thenForbidden() {
        DeleteItemRequest request = new DeleteItemRequest(1L, 1L);

        ResponseEntity<String> response = dashboardController.deleteItem(nonAdminToken, request);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    // Invalid tokens tests
    @Test
    void whenGetOrdersWithInvalidToken_thenThrowIllegalArgumentException() {
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            dashboardController.getOrders(invalidToken);
        });

        assertNotNull(thrownException.getMessage());
    }

    @Test
    void whenUpdateOrderStatusWithInvalidToken_thenThrowIllegalArgumentException() {
        UpdateOrderRequest request = new UpdateOrderRequest(1L, "NEW_STATUS");

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            dashboardController.updateOrderStatus(invalidToken, request);
        });

        assertNotNull(thrownException.getMessage());
    }

    @Test
    void whenDeleteOrderWithInvalidToken_thenThrowIllegalArgumentException() {
        DeleteOrderRequest request = new DeleteOrderRequest(1L);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            dashboardController.deleteOrder(invalidToken, request);
        });

        assertNotNull(thrownException.getMessage());
    }

    @Test
    void whenDeleteItemWithInvalidToken_thenThrowIllegalArgumentException() {
        DeleteItemRequest request = new DeleteItemRequest(1L, 1L);

        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {
            dashboardController.deleteItem(invalidToken, request);
        });

        assertNotNull(thrownException.getMessage());
    }
}
