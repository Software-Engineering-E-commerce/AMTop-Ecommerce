package com.example.BackEnd.Services.SortServices;

import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SortProducts<T extends Comparable<T>> implements ISortStrategy<Product> {

    private final ProductRepository productRepository;

    @Override
    public List<Product> sort(String sortBy, boolean sortOrder) {
        if (sortBy.equals("averageRating")) {
            if (sortOrder) {
                return productRepository.findAllOrderByAverageRatingAsc();
            } else {
                return productRepository.findAllOrderByAverageRatingDesc();
            }
        }
        if (sortBy.equals("numberOfReviews")) {
            if (sortOrder) {
                return productRepository.findAllOrderByNumberOfReviewsAsc();
            } else {
                return productRepository.findAllOrderByNumberOfReviewsDesc();
            }
        }
        if (sortOrder) {
            return productRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
        } else {
            return productRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
        }
    }
}
