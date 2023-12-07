package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.OrderItem;
import com.example.BackEnd.Model.OrderItemPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
    List<OrderItem> findByOrderId(Long orderId);
}
