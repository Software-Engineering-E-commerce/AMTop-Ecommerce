package com.example.BackEnd.Services.SortServices;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SortService<T extends Comparable<T>> {

    private final SortProducts sortProducts;
    private final SortOrders sortOrders;

    public List<T> sort(String entity, String sortBy, boolean sortOrder) {
        try {
            return switch (entity) {
                case "Product" -> sortProducts.sort(sortBy, sortOrder);
                case "Order" -> sortOrders.sort(sortBy, sortOrder);
                default -> throw new Exception();
            };
        } catch (Exception e) {
            return null;
        }
    }
}
