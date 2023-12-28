package com.example.BackEnd.Services.SortServices;

import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.OrderRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class SortOrdersTest {
    Order order1, order2, order3, order4;
    Category category1, category2, category3;
    Product product1, product2, product3, product4;
    Customer customer1, customer2, customer3, customer4;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private SortOrders sortOrdersOrders;

    @BeforeEach
    void setUp() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        customer1 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer1.json"), Customer.class);
        customer2 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer2.json"), Customer.class);
        customer3 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer3.json"), Customer.class);
        customer4 = objectMapper.readValue(new File(
                "src/test/resources/customers/customer4.json"), Customer.class);
        customerRepository.save(customer1);
        customerRepository.save(customer2);
        customerRepository.save(customer3);
        customerRepository.save(customer4);

        category1 = objectMapper.readValue(new File(
                "src/test/resources/categories/category1.json"), Category.class);
        category2 = objectMapper.readValue(new File(
                "src/test/resources/categories/category2.json"), Category.class);
        category3 = objectMapper.readValue(new File(
                "src/test/resources/categories/category3.json"), Category.class);
        categoryRepository.save(category1);
        categoryRepository.save(category2);
        categoryRepository.save(category3);

        product1 = objectMapper.readValue(new File(
                "src/test/resources/products/product1.json"), Product.class);
        product2 = objectMapper.readValue(new File(
                "src/test/resources/products/product2.json"), Product.class);
        product3 = objectMapper.readValue(new File(
                "src/test/resources/products/product3.json"), Product.class);
        product4 = objectMapper.readValue(new File(
                "src/test/resources/products/product4.json"), Product.class);
        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);

        // order 1:
        order1 = objectMapper.readValue(new File(
                "src/test/resources/orders/order1.json"), Order.class);
        order1.setCustomer(customer2);
        List<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItem(order1, product1, product1.getPrice(), 2));
        orderItems1.add(new OrderItem(order1, product2, product2.getPrice(), 1));
        order1.setOrderItems(orderItems1);

        // order 2:
        order2 = objectMapper.readValue(new File(
                "src/test/resources/orders/order2.json"), Order.class);
        order2.setCustomer(customer1);
        List<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(new OrderItem(order2, product1, product1.getPrice(), 1));
        orderItems2.add(new OrderItem(order2, product2, product2.getPrice(), 1));
        orderItems2.add(new OrderItem(order2, product3, product3.getPrice(), 2));
        order2.setOrderItems(orderItems2);

        // order 3:
        order3 = objectMapper.readValue(new File(
                "src/test/resources/orders/order3.json"), Order.class);
        order3.setCustomer(customer4);
        List<OrderItem> orderItems3 = new ArrayList<>();
        orderItems3.add(new OrderItem(order3, product4, product4.getPrice(), 2));
        order3.setOrderItems(orderItems3);

        // order 4:
        order4 = objectMapper.readValue(new File(
                "src/test/resources/orders/order4.json"), Order.class);
        order4.setCustomer(customer3);
        List<OrderItem> orderItems4 = new ArrayList<>();
        orderItems4.add(new OrderItem(order4, product1, product1.getPrice(), 1));
        order4.setOrderItems(orderItems4);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
    }

    // sort by order id ascending
    // expected: order1, order2, order3, order4
    @Test
    @Transactional
    void sortByIdAsc() {
        List<Order> expected = List.of(order1, order2, order3, order4);
        List<Order> actual = sortOrdersOrders.sort("id", true);
        assertEquals(expected, actual);
    }

    // sort by order id descending
    // expected: order4, order3, order2, order1
    @Test
    @Transactional
    void sortByIdDesc() {
        List<Order> expected = List.of(order4, order3, order2, order1);
        List<Order> actual = sortOrdersOrders.sort("id", false);
        assertEquals(expected, actual);
    }

    // sort by order start date ascending
    // expected: order3, order1, order2, order4
    @Test
    @Transactional
    void sortByStartDateAsc() {
        List<Order> expected = List.of(order3, order1, order2, order4);
        List<Order> actual = sortOrdersOrders.sort("startDate", true);
        assertEquals(expected, actual);
    }

    // sort by order start date descending
    // expected: order4, order2, order1, order3
    @Test
    @Transactional
    void sortByStartDateDesc() {
        List<Order> expected = List.of(order4, order2, order1, order3);
        List<Order> actual = sortOrdersOrders.sort("startDate", false);
        assertEquals(expected, actual);
    }

    // sort by order delivery date ascending
    // expected: order3, order1, order2, order4
    @Test
    @Transactional
    void sortByDeliveryDateAsc() {
        List<Order> expected = List.of(order3, order1, order2, order4);
        List<Order> actual = sortOrdersOrders.sort("deliveryDate", true);
        assertEquals(expected, actual);
    }

    // sort by order delivery date descending
    // expected: order4, order2, order1, order3
    @Test
    @Transactional
    void sortByDeliveryDateDesc() {
        List<Order> expected = List.of(order4, order2, order1, order3);
        List<Order> actual = sortOrdersOrders.sort("deliveryDate", false);
        assertEquals(expected, actual);
    }

    // sort by customer id ascending
    // expected: order2, order1, order4, order3
    @Test
    @Transactional
    void sortByCustomerIdAsc() {
        List<Order> expected = List.of(order2, order1, order4, order3);
        List<Order> actual = sortOrdersOrders.sort("customer.id", true);
        assertEquals(expected, actual);
    }

    // sort by customer id descending
    // expected: order3, order4, order1, order2
    @Test
    @Transactional
    void sortByCustomerIdDesc() {
        List<Order> expected = List.of(order3, order4, order1, order2);
        List<Order> actual = sortOrdersOrders.sort("customer.id", false);
        assertEquals(expected, actual);
    }

    // sort by number of order items ascending
    // expected: order2, order1, order3, order4
    @Test
    @Transactional
    void sortByNumberOfOrderItemsAsc() {
        List<Order> expected = List.of(order4, order3, order1, order2);
        List<Order> actual = sortOrdersOrders.sort("totalAmount", true);
        assertEquals(expected, actual);
    }

    // sort by number of order items descending
    // expected: order4, order3, order1, order2
    @Test
    @Transactional
    void sortByNumberOfOrderItemsDesc() {
        List<Order> expected = List.of(order2, order1, order3, order4);
        List<Order> actual = sortOrdersOrders.sort("totalAmount", false);
        assertEquals(expected, actual);
    }

    // sort by total cost ascending
    // expected: order2, order1, order4, order3
    @Test
    @Transactional
    void sortByTotalCostAsc() {
        List<Order> expected = List.of(order3, order4, order1, order2);
        List<Order> actual = sortOrdersOrders.sort("totalCost", true);
        assertEquals(expected, actual);
    }

    // sort by total cost descending
    // expected: order3, order4, order1, order2
    @Test
    @Transactional
    void sortByTotalCostDesc() {
        List<Order> expected = List.of(order2, order1, order4, order3);
        List<Order> actual = sortOrdersOrders.sort("totalCost", false);
        assertEquals(expected, actual);
    }

    // sort by order status ascending
    // expected: order2, order3, order1, order4
    @Test
    @Transactional
    void sortByOrderStatusAsc() {
        List<Order> expected = List.of(order2, order3, order1, order4);
        List<Order> actual = sortOrdersOrders.sort("status", true);
        assertEquals(expected, actual);
    }

    // sort by order status descending
    // expected: order4, order1, order3, order2
    @Test
    @Transactional
    void sortByOrderStatusDesc() {
        List<Order> expected = List.of(order4, order1, order2, order3);
        List<Order> actual = sortOrdersOrders.sort("status", false);
        assertEquals(expected, actual);
    }

}