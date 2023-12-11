package com.example.BackEnd.Repositories;

import com.example.BackEnd.Model.CustomerProductPK;
import com.example.BackEnd.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, CustomerProductPK> {
}
