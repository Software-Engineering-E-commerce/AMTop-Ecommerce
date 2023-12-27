package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.Model.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterOrders<T extends Comparable<T>> implements IFilter {

    private final EntityManager entityManager;

    @Override
    public List<Order> filter(Object criteria) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);

        Class<?> clazz = criteria.getClass();
        Field[] fields = clazz.getDeclaredFields();

        List<Predicate> predicates = new ArrayList<>();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(criteria);

                if (value != null) {
                    predicates.add(criteriaBuilder.equal(root.get(field.getName()), value));
                }
            } catch (IllegalAccessException e) {
                return null;
            }
        }

        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
