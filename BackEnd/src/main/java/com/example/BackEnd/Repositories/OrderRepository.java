package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(Customer customer);
}

