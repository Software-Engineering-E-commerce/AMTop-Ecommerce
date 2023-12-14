package com.example.BackEnd.Repositories;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerProductPK;
import com.example.BackEnd.Model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, CustomerProductPK> {
    Optional<WishList> findByCustomer_IdAndProduct_Id (Long customerId, Long productId);

    // Function that returns a boolean if the product is in
    boolean existsByCustomer_IdAndProduct_Id(Long customerId, Long productId);

    // Function to delete a product from the wishlist of the customer
    void deleteByCustomerAndProduct_Id(Customer customer, Long productId);

    // Function to get all the products in the wishlist of the customer given his id
    List<WishList> findByCustomer_Id(Long customerId);
}
