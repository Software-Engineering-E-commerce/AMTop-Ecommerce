package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.DTO.FilterOrderDto;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.Order;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterOrders<T extends Comparable<T>> implements IFilter {

    private final EntityManager entityManager;

    private static List<Predicate> getPredicates(Object criteria, CriteriaBuilder criteriaBuilder, Root<Order> root) {
        List<Predicate> predicates = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        FilterOrderDto filterOrderDto = objectMapper.convertValue(criteria, FilterOrderDto.class);
        setIdPredicates(criteriaBuilder, root, predicates, filterOrderDto);
        setRangesPredicates(criteriaBuilder, root, predicates, filterOrderDto);
        setStringsPredicates(criteriaBuilder, root, filterOrderDto, predicates);
        return predicates;
    }

    private static void setIdPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Order> root,
            List<Predicate> predicates,
            FilterOrderDto filterOrderDto
    ) {
        if (filterOrderDto.getId() != 0) {
            predicates.add(criteriaBuilder
                    .equal(root.get("id"), filterOrderDto.getId()));
        }
        if (filterOrderDto.getCustomerId() != 0) {
            Join<Order, Customer> customerJoin = root.join("customer");
            predicates.add(criteriaBuilder.
                    equal(customerJoin.get("id"), filterOrderDto.getCustomerId()));
        }
    }

    private static void setRangesPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Order> root,
            List<Predicate> predicates,
            FilterOrderDto filterOrderDto
    ) {
        predicates.add(criteriaBuilder
                .greaterThanOrEqualTo(root.get("totalCost"), filterOrderDto.getFromPrice()));
        predicates.add(criteriaBuilder
                .lessThanOrEqualTo(root.get("totalCost"), filterOrderDto.getToPrice()));
    }

    private static void setStringsPredicates(
            CriteriaBuilder criteriaBuilder,
            Root<Order> root,
            FilterOrderDto filterOrderDto,
            List<Predicate> predicates
    ) {
        if (!filterOrderDto.getStatus().isEmpty()) {
            predicates.add(criteriaBuilder
                    .equal(root.get("status"), filterOrderDto.getStatus()));
        }
    }

    @Override
    public List<Order> filter(Object criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        List<Predicate> predicates = getPredicates(criteria, criteriaBuilder, root);
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
