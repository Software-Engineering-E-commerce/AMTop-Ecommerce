package com.example.BackEnd.Model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerAddressTest {
    @Test
    public void testCustomerAddressConstructor() {
        // Set up
        Customer customer = new Customer();
        customer.setId(1L);
        String address = "123 Main St";

        // Create CustomerAddress using constructor
        CustomerAddress customerAddress = new CustomerAddress(customer, address);

        // Verify
        assertEquals(customer, customerAddress.getCustomer());
        assertEquals(address, customerAddress.getAddress());
    }

    @Test
    public void testCustomerAddressPKConstructor() {
        // Set up
        Customer customer = new Customer();
        customer.setId(1L);
        String address = "123 Main St";

        // Create CustomerAddressPK using constructor
        CustomerAddressPK customerAddressPK = new CustomerAddressPK(customer, address);

        // Verify
        assertEquals(customer, customerAddressPK.getCustomer());
        assertEquals(address, customerAddressPK.getAddress());
    }

    @Test
    public void testCustomerAddressEqualsAndHashCode() {
        // Set up
        Customer customer1 = new Customer();
        customer1.setId(1L);
        String address1 = "123 Main St";

        Customer customer2 = new Customer();
        customer2.setId(1L);
        String address2 = "123 Main St";

        Customer customer3 = new Customer();
        customer3.setId(2L);
        String address3 = "456 Oak St";

        CustomerAddress ca1 = new CustomerAddress(customer1, address1);
        CustomerAddress ca2 = new CustomerAddress(customer2, address2);
        CustomerAddress ca3 = new CustomerAddress(customer3, address3);

        // Verify
        assertEquals(ca1, ca2);
        assertNotEquals(ca1, ca3);
        assertEquals(ca1.hashCode(), ca2.hashCode());
        assertNotEquals(ca1.hashCode(), ca3.hashCode());
    }

    @Test
    public void testCustomerAddressPKEqualsAndHashCode() {
        // Set up
        Customer customer1 = new Customer();
        customer1.setId(1L);
        String address1 = "123 Main St";

        Customer customer2 = new Customer();
        customer2.setId(1L);
        String address2 = "123 Main St";

        Customer customer3 = new Customer();
        customer3.setId(2L);
        String address3 = "456 Oak St";

        CustomerAddressPK capk1 = new CustomerAddressPK(customer1, address1);
        CustomerAddressPK capk2 = new CustomerAddressPK(customer2, address2);
        CustomerAddressPK capk3 = new CustomerAddressPK(customer3, address3);

        // Verify
        assertEquals(capk1, capk2);
        assertNotEquals(capk1, capk3);
        assertEquals(capk1.hashCode(), capk2.hashCode());
        assertNotEquals(capk1.hashCode(), capk3.hashCode());
    }
}