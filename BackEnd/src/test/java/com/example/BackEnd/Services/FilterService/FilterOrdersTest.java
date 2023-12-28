package com.example.BackEnd.Services.FilterService;

import com.example.BackEnd.DTO.FilterOrderDto;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.OrderRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
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
class FilterOrdersTest {

    Order order1, order2, order3, order4;
    Category category1, category2, category3;
    Product product1, product2, product3, product4;
    Customer customer1, customer2, customer3, customer4;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private FilterOrders filterOrders;

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
        order1.setCustomer(customer1);
        List<OrderItem> orderItems1 = new ArrayList<>();
        orderItems1.add(new OrderItem(order1, product1, product1.getPrice(), 2));
        orderItems1.add(new OrderItem(order1, product2, product2.getPrice(), 1));
        order1.setOrderItems(orderItems1);

        // order 2:
        order2 = objectMapper.readValue(new File(
                "src/test/resources/orders/order2.json"), Order.class);
        order2.setCustomer(customer2);
        List<OrderItem> orderItems2 = new ArrayList<>();
        orderItems2.add(new OrderItem(order2, product1, product1.getPrice(), 1));
        orderItems2.add(new OrderItem(order2, product2, product2.getPrice(), 1));
        orderItems2.add(new OrderItem(order2, product3, product3.getPrice(), 2));
        order2.setOrderItems(orderItems2);

        // order 3:
        order3 = objectMapper.readValue(new File(
                "src/test/resources/orders/order3.json"), Order.class);
        order3.setCustomer(customer3);
        List<OrderItem> orderItems3 = new ArrayList<>();
        orderItems3.add(new OrderItem(order3, product4, product4.getPrice(), 2));
        order3.setOrderItems(orderItems3);

        // order 4:
        order4 = objectMapper.readValue(new File(
                "src/test/resources/orders/order4.json"), Order.class);
        order4.setCustomer(customer4);
        List<OrderItem> orderItems4 = new ArrayList<>();
        orderItems4.add(new OrderItem(order4, product1, product1.getPrice(), 1));
        order4.setOrderItems(orderItems4);

        orderRepository.save(order1);
        orderRepository.save(order2);
        orderRepository.save(order3);
        orderRepository.save(order4);
    }

    // Test case 1: filter by customer id
    @Test
    @Transactional
    void filterByCustomerId() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(customer1.getId());
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(0);
        filterOrderDto.setToPrice(1000000000);
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(customer1.getId(), orders.get(0).getId());
    }

    // Test case 2: filter by order id
    @Test
    @Transactional
    void filterByOrderId() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(0L);
        filterOrderDto.setId(order2.getId());
        filterOrderDto.setFromPrice(0);
        filterOrderDto.setToPrice(1000000000);
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(order2.getId(), orders.get(0).getId());
    }

    // Test case 3: filter by order id and customer id
    @Test
    @Transactional
    void filterByOrderIdAndCustomerId() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(customer2.getId());
        filterOrderDto.setId(order2.getId());
        filterOrderDto.setFromPrice(0);
        filterOrderDto.setToPrice(1000000000);
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(order2.getId(), orders.get(0).getId());
        assertEquals(customer2.getId(), orders.get(0).getCustomer().getId());
    }

    // Test case 4: filter by price range
    @Test
    @Transactional
    void filterByPriceRange() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(0L);
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(100);
        filterOrderDto.setToPrice(210);
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(3, orders.size());
        assertEquals(order1.getId(), orders.get(0).getId());
        assertEquals(order2.getId(), orders.get(1).getId());
        assertEquals(order4.getId(), orders.get(2).getId());
    }

    // Test case 5: filter by status
    @Test
    @Transactional
    void filterByStatus() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(0L);
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(0);
        filterOrderDto.setToPrice(1000000000);
        filterOrderDto.setStatus("delivered");
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(2, orders.size());
        assertEquals(order2.getId(), orders.get(0).getId());
        assertEquals(order3.getId(), orders.get(1).getId());
    }

    // Test case 6: filter by status and price range
    @Test
    @Transactional
    void filterByStatusAndPriceRange() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(0L);
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(100);
        filterOrderDto.setToPrice(210);
        filterOrderDto.setStatus("delivered");
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(order2.getId(), orders.get(0).getId());
    }

    // Test case 7: filter by status and customer id
    @Test
    @Transactional
    void filterByStatusAndCustomerId() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(customer1.getId());
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(0);
        filterOrderDto.setToPrice(1000000000);
        filterOrderDto.setStatus("delivered");
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(0, orders.size());
    }

    // Test case 8: filter by status, customer id and price range
    @Test
    @Transactional
    void filterByStatusAndCustomerIdAndPriceRange() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(customer3.getId());
        filterOrderDto.setId(0L);
        filterOrderDto.setFromPrice(10);
        filterOrderDto.setToPrice(100);
        filterOrderDto.setStatus("delivered");
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(order3.getId(), orders.get(0).getId());
        assertEquals(order3.getCustomer().getId(), orders.get(0).getCustomer().getId());
    }

    // Test case 9: filter by order id, status, customer id and price range
    @Test
    @Transactional
    void filterByOrderIdAndStatusAndCustomerIdAndPriceRange() {
        FilterOrderDto filterOrderDto = new FilterOrderDto();
        filterOrderDto.setCustomerId(customer3.getId());
        filterOrderDto.setId(order3.getId());
        filterOrderDto.setFromPrice(10);
        filterOrderDto.setToPrice(100);
        filterOrderDto.setStatus("delivered");
        List<Order> orders = filterOrders.filter(filterOrderDto);
        assertEquals(1, orders.size());
        assertEquals(order3.getId(), orders.get(0).getId());
        assertEquals(order3.getCustomer().getId(), orders.get(0).getCustomer().getId());
    }
}