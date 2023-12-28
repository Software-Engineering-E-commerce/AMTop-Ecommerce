package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.DTO.FilterProductDto;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class FilterProducts<T extends Comparable<T>> implements IFilter {

    private final EntityManager entityManager;

    private static List<Predicate> getPredicates(Object criteria, CriteriaBuilder criteriaBuilder, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        FilterProductDto filterProductDto = objectMapper.convertValue(criteria, FilterProductDto.class);
        setRangesPredicates(criteriaBuilder, root, predicates, filterProductDto);
        setStringsPredicates(criteriaBuilder, root, filterProductDto, predicates);
        return predicates;
    }

    private static void setRangesPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Product> root,
            List<Predicate> predicates,
            FilterProductDto filterProductDto
    ) {
        predicates.add(criteriaBuilder
                .greaterThanOrEqualTo(root.get("price"), filterProductDto.getFromPrice()));
        predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("price"), filterProductDto.getToPrice()));

        predicates.add(criteriaBuilder
                .greaterThanOrEqualTo(root.get("discountPercentage"), filterProductDto.getFromDiscountPercentage()));
        predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("discountPercentage"), filterProductDto.getToDiscountPercentage()));
    }

    private static void setStringsPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Product> root,
            FilterProductDto filterProductDto,
            List<Predicate> predicates
    ) {
        if (!Objects.equals(filterProductDto.getProductName(), "")) {
            predicates.add(criteriaBuilder
                    .like(root.get("productName"), "%" + filterProductDto.getProductName() + "%"));
        }
        if (!Objects.equals(filterProductDto.getDescription(), "")) {
            predicates.add(criteriaBuilder
                    .like(root.get("description"), "%" + filterProductDto.getDescription() + "%"));
        }
        if (!Objects.equals(filterProductDto.getBrand(), "")) {
            predicates.add(criteriaBuilder
                    .equal(root.get("brand"), filterProductDto.getBrand()));
        }
        if (!Objects.equals(filterProductDto.getCategory(), "")) {
            Join<Product, Category> categoryJoin = root.join("category");
            predicates.add(criteriaBuilder.
                    equal(categoryJoin.get("categoryName"), filterProductDto.getCategory()));
        }
        if (filterProductDto.isAvailable()) {
            predicates.add(criteriaBuilder
                    .greaterThan(root.get("productCountAvailable"), 0));
        }
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
