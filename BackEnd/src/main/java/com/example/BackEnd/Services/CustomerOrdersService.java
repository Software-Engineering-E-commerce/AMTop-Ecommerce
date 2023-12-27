package com.example.BackEnd.Services;

import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerOrdersService {
    private final OrderRepository orderRepository;
    public List<Order> retrieveOrders(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }
}
