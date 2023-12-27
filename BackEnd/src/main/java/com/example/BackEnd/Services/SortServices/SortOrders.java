package com.example.BackEnd.Services.SortServices;

import com.example.BackEnd.Model.Order;
import com.example.BackEnd.Repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SortOrders<T extends Comparable<T>> implements ISortStrategy<Order> {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> sort(String sortBy, boolean sortOrder) {
        if (sortOrder) {
            return orderRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            return orderRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
        }
    }
}
