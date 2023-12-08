package com.example.BackEnd.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(OrderItemPK.class)
public class OrderItem {
    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Id
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private float originalCost;
    @Column(nullable = false)
    private int quantity;

}
