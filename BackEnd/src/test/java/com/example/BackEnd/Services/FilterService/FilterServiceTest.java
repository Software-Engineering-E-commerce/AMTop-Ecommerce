package com.example.BackEnd.Services.FilterService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class FilterServiceTest<T extends Comparable<T>> {
    @Mock
    private FilterProducts filterProducts;

    @Mock
    private FilterOrders filterOrders;

    private FilterService<String> filterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        filterService = new FilterService<>(filterProducts, filterOrders);
    }

    @Test
    public void testFilterOrder() {
        // Arrange
        when(filterOrders.filter(any())).thenReturn(Arrays.asList("order1", "order2"));

        // Act
        List<String> result = filterService.filter("Order", "criteria", 1L);

        // Assert
        assertEquals(Arrays.asList("order1", "order2"), result);
        verify(filterOrders, times(1)).filter("criteria");
        verify(filterProducts, times(0)).filter(any());
    }

    @Test
    public void testFilterUnknownEntity() {
        // Act
        List<String> result = filterService.filter("UnknownEntity", "criteria", 1L);

        // Assert
        assertNull(result);
        verify(filterProducts, times(0)).filter(any());
        verify(filterOrders, times(0)).filter(any());
    }

    @Test
    public void testFilterException() {
        // Arrange
        when(filterProducts.filter(any())).thenThrow(new RuntimeException("Filter exception"));

        // Act
        List<String> result = filterService.filter("Product", "criteria", 1L);

        // Assert
        assertEquals(new ArrayList<>(), result);
//        verify(filterProducts, times(1)).filter("criteria");
        verify(filterOrders, times(0)).filter(any());
    }
}