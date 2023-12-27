package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.DTO.FilterProductDto;
import com.example.BackEnd.Model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilterProducts<T extends Comparable<T>> implements IFilter {

    private final EntityManager entityManager;

    private static List<Predicate> getPredicates(Object criteria, CriteriaBuilder criteriaBuilder, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        FilterProductDto filterProductDto = objectMapper.convertValue(criteria, FilterProductDto.class);

        predicates.add(criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), filterProductDto.getFromPrice()));
        predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), filterProductDto.getToPrice()));

        predicates.add(criteriaBuilder
                .greaterThanOrEqualTo(root.get("discountPercentage"), filterProductDto.getFromDiscountPercentage()));
        predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("discountPercentage"), filterProductDto.getToDiscountPercentage()));

        if (filterProductDto.getProductName() != null) {
            predicates.add(criteriaBuilder
                    .like(root.get("productName"), "%" + filterProductDto.getProductName() + "%"));
        }
        if (filterProductDto.getDescription() != null) {
            predicates.add(criteriaBuilder
                    .like(root.get("description"), "%" + filterProductDto.getDescription() + "%"));
        }
        if (filterProductDto.getBrand() != null) {
            predicates.add(criteriaBuilder
                    .equal(root.get("brand"), filterProductDto.getBrand()));
        }
        if (filterProductDto.getCategory() != null) {
            predicates.add(criteriaBuilder
                    .equal(root.get("category"), filterProductDto.getCategory()));
        }
        if (filterProductDto.isAvailable()) {
            predicates.add(criteriaBuilder
                    .greaterThan(root.get("productCountAvailable"), 0));
        }
        return predicates;
    }

    @Override
    public List<Product> filter(Object criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        List<Predicate> predicates = getPredicates(criteria, criteriaBuilder, root);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
