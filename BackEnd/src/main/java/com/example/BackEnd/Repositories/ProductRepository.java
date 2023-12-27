package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductById(Long productId);

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p.id ORDER BY AVG(r.rating) ASC")
    List<Product> findAllOrderByAverageRatingAsc();

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p.id ORDER BY AVG(r.rating) DESC")
    List<Product> findAllOrderByAverageRatingDesc();

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p.id ORDER BY COUNT(*) ASC")
    List<Product> findAllOrderByNumberOfReviewsAsc();

    @Query("SELECT p FROM Product p LEFT JOIN p.reviews r GROUP BY p.id ORDER BY COUNT(*) DESC")
    List<Product> findAllOrderByNumberOfReviewsDesc();
}
