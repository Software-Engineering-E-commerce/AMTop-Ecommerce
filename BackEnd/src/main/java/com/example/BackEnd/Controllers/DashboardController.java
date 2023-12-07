package com.example.BackEnd.Controllers;

import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Model.OrderItem;
import com.example.BackEnd.Services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dash")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class DashboardController {
    // This controller is not compilable.
    // It requires the Dashboard Service implementation to become functional
    // Merge with branch EC-105 to have a functional controller
    private final DashboardService dashboardService;

    @GetMapping("/getOrders")
    public ResponseEntity<List<Order>> getOrders() {
        return ResponseEntity.ok(dashboardService.retrieveOrders());
    }

    @PostMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestBody Long orderId) {
        if(dashboardService.cancelOrder(orderId)) return ResponseEntity.ok("Order Deleted");
        else return ResponseEntity.ok("Order Not Found");
    }

    @GetMapping("/getOrderItems")
    public ResponseEntity<List<OrderItem>> getOrderItems(@RequestBody Long orderId) {
        return ResponseEntity.ok(dashboardService.getOrderItems(orderId));
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Long orderId, @RequestBody String newStatus) {
        if(dashboardService.updateOrderStatus(orderId, newStatus)) return ResponseEntity.ok("Order Status Updated");
        else return ResponseEntity.ok("Order Not Found Or Status Couldn't Change");
    }
}
