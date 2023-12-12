package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.CustomerProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerCartRepository extends JpaRepository<CustomerCart, CustomerProductPK> {
    List<CustomerCart> findByCustomer_Id(Long customerId);

    Optional<CustomerCart> findByCustomerAndProduct_Id(Customer customer, Long productId);

    Optional<CustomerCart> findByCustomer_IdAndProduct_Id(Long customerId, Long productId);

    // Check if a specific product exists in the customer's cart
    boolean existsByCustomerAndProduct_Id(Customer customer, Long productId);

    // Delete a product from customer's cart given the customer object and productID
    void deleteByCustomerAndProduct_Id(Customer customer, Long productId);
    // Delete all the products in customer's cart
    void deleteByCustomer(Customer customer);
}
