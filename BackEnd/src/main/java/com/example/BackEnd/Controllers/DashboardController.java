package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.DeleteItemRequest;
import com.example.BackEnd.DTO.DeleteOrderRequest;
import com.example.BackEnd.DTO.UpdateOrderRequest;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.*;
import com.example.BackEnd.Services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dash")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final JwtService jwtService;
    private final AdminRepository adminRepository;

    public boolean isAdmin(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7); // Skip "Bearer " prefix
            String email = jwtService.extractUsername(token);
            Optional<Admin> adminCheck = adminRepository.findByEmail(email);
            return adminCheck.isPresent();
        } else {
            throw new IllegalArgumentException("Authorization header doesn't exist or is in the wrong format");
        }
    }

    @GetMapping("/getOrders")
    public ResponseEntity<List<Order>> getOrders(@RequestHeader("Authorization") String authorizationHeader) {
        if(!isAdmin(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        } else {
            return ResponseEntity.ok(dashboardService.retrieveOrders());
        }
    }

    @PostMapping("/updateOrderStatus")
    public ResponseEntity<String> updateOrderStatus(@RequestHeader("Authorization") String authorizationHeader,
                                                    @RequestBody UpdateOrderRequest request) {
        if(!isAdmin(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        } else {
            return ResponseEntity.ok(dashboardService.updateOrderStatus(request.getOrderId(), request.getNewStatus()));
        }
    }

    @DeleteMapping("/deleteOrder")
    public ResponseEntity<String> deleteOrder(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody DeleteOrderRequest request) {
        if(!isAdmin(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        } else {
            return ResponseEntity.ok(dashboardService.cancelOrder(request.getOrderId()));
        }
    }

    @DeleteMapping("/deleteItem")
    public ResponseEntity<String> deleteItem(@RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody DeleteItemRequest request) {
        if(!isAdmin(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access Denied");
        } else {
            return ResponseEntity.ok(dashboardService.deleteOrderItem(request.getOrderId(), request.getProductId()));
        }
    }
}