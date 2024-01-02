package com.example.BackEnd.Services;

import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchService {

    private final EntityManager entityManager;

    private static List<Predicate> getPredicates(String key, CriteriaBuilder criteriaBuilder, Root<Product> root) {
        List<Predicate> predicates = new ArrayList<>();
        setStringsPredicates(criteriaBuilder, root, key, predicates);
        return predicates;
    }

    private static void setStringsPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Product> root,
            String key,
            List<Predicate> predicates
    ) {
        if (key != null) {
            predicates.add(criteriaBuilder
                    .like(root.get("productName"), "%" + key + "%"));
            predicates.add(criteriaBuilder
                    .like(root.get("description"), "%" + key + "%"));
            predicates.add(criteriaBuilder
                    .like(root.get("brand"), "%" + key + "%"));
            Join<Product, Category> categoryJoin = root.join("category");
            predicates.add(criteriaBuilder.
                    like(categoryJoin.get("categoryName"), "%" + key + "%"));
        }
    }

    public List<Product> search(String key) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
        Root<Product> root = criteriaQuery.from(Product.class);
        List<Predicate> predicates = getPredicates(key, criteriaBuilder, root);
        criteriaQuery.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
