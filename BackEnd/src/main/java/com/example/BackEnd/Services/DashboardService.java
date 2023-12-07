package com.example.BackEnd.Services;

import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Model.OrderItem;
import com.example.BackEnd.Repositories.OrderItemRepository;
import com.example.BackEnd.Repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public List<Order> retrieveOrders() {
        return orderRepository.findAll();
    }

    public boolean cancelOrder(Long orderId) {
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if(orderToDelete.isPresent()) {
            orderRepository.delete(orderToDelete.get());
            return true;
        } else return false;
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);
        if(!items.isEmpty()) {
            return items;
        } else return new ArrayList<>(0);
    }

    public boolean updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderToUpdate = orderRepository.findById(orderId);
        if(orderToUpdate.isPresent()) {
            Order order = orderToUpdate.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
            return true;
        } else return false;
    }
}
