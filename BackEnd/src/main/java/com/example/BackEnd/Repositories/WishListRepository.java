package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.CustomerProductPK;
import com.example.BackEnd.Model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, CustomerProductPK> {
    Optional<WishList> findByCustomer_IdAndProduct_Id (Long customerId, Long productId);
}
