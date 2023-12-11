package com.example.BackEnd.Services;

import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Model.OrderItem;
import com.example.BackEnd.Model.OrderItemPK;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.OrderItemRepository;
import com.example.BackEnd.Repositories.OrderRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public List<Order> retrieveOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public String cancelOrder(Long orderId) {
        Optional<Order> orderToDelete = orderRepository.findById(orderId);
        if (orderToDelete.isPresent()) {
            orderRepository.delete(orderToDelete.get());
            return "Order deleted";
        } else return "Order not found, please refresh the page and try again. If the problem persists," +
                " please contact one of the development team members.";
    }

    @Transactional
    public String updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderToUpdate = orderRepository.findById(orderId);
        if (orderToUpdate.isPresent()) {
            Order order = orderToUpdate.get();
            order.setStatus(newStatus);
            orderRepository.save(order);
            return "Status updated";
        } else return "Order not found, please refresh the page and try again. If the problem persists," +
                " please contact one of the development team members.";
    }

    @Transactional
    public String deleteOrderItem(Long orderId, Long productId) {
        Optional<Order> order = orderRepository.findById(orderId);
        Optional<Product> product = productRepository.findById(productId);
        if (order.isPresent() && product.isPresent()) {
            OrderItemPK itemPK = new OrderItemPK(order.get(), product.get());
            Optional<OrderItem> itemToDelete = orderItemRepository.findById(itemPK);
            if (itemToDelete.isPresent()) {
                orderItemRepository.delete(itemToDelete.get());
                return "Item deleted";
            } else return "Item not found, please refresh the page and try again. If the problem persists," +
                    " please contact one of the development team members.";
        } else {
            if (order.isPresent())
                return "The product associated with the item is missing from the database," +
                        " please contact one of the development team members.";
            else if (product.isPresent())
                return "The order associated with the item is missing from the database," +
                        " please contact one of the development team members.";
            else
                return "Both the order and the product associated with the item are missing from the database," +
                        " please contact one of the development team members.";
        }
    }
}