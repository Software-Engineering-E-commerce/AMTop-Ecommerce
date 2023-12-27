package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.CustomerProductPK;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, CustomerProductPK> {
    List<Review> findByProduct(Product product);
}
