package com.example.BackEnd.Services.FilterService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterService<T extends Comparable<T>> {

    private final FilterProducts filterProducts;
    private final FilterOrders filterOrders;

    public List<T> filter(String entity, Object criteria, Long customerId) {
        try {
            return switch (entity) {
                case "Product" -> filterProducts.getFilteredDTOList(criteria, customerId);
                case "Order" -> filterOrders.filter(criteria);
                default -> throw new Exception();
            };
        } catch (Exception e) {
            return null;
        }
    }

}
