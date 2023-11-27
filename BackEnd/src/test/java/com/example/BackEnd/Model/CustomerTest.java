package com.example.BackEnd.Model;

import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerAddressRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerTest {
    @Autowired
    private CustomerRepository customerRepository;
    @Mock
    private CustomerRepository mockedCustomerRepository;

    @Test
    public void testCustomerDefaultConstructor() {
        Customer customer = new Customer();
        assertNotNull(customer);
        assertTrue(customer.getAddresses().isEmpty());
        assertNull(customer.getEmail());
        assertNull(customer.getPassword());
    }

    @Test
    public void testCustomerWithAddresses() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        List<CustomerAddress> addresses = new ArrayList<>();
        CustomerAddress address1 = new CustomerAddress(customer, "elmontazah");
        CustomerAddress address2 = new CustomerAddress(customer, "elmandra");
        CustomerAddress address3 = new CustomerAddress(customer, "sidibeshr");
        addresses.add(address1); addresses.add(address2);
        customer.setAddresses(addresses);
        assertNotNull(customer);
        assertEquals(customer.getPassword(), "password");
        assertEquals(customer.getAddresses().size(), 2);
        customer.getAddresses().add(address3);
        assertEquals(customer.getAddresses().size(), 3);
    }
    @Test
    public void testMockedCustomerRepoCreateRead() {
        String email = "test@example.com";
        Customer customer = getCustomer(email);

        when(mockedCustomerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer savedCustomer = mockedCustomerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertEquals(email, savedCustomer.getEmail());
        assertEquals("password", savedCustomer.getPassword());
        assertEquals(customer.getAddresses().size(), 2);
        assertNotEquals(savedCustomer.getFirstName(), null);
        assertNotEquals(savedCustomer.getLastName(), "Zian");

    }

    private static Customer getCustomer(String email) {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setEmail(email);
        customer.setPassword("password");
        customer.setFirstName("mahmoud");
        customer.setLastName("Attia");
        customer.setIsGmail(false);
        customer.setIsVerified(false);
        List<CustomerAddress> addresses = new ArrayList<>();
        CustomerAddress address1 = new CustomerAddress(customer, "elmontazah");
        CustomerAddress address2 = new CustomerAddress(customer, "elmandra");
        addresses.add(address1);
        addresses.add(address2);
        customer.setAddresses(addresses);
        return customer;
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCustomerRepoCreateRead() {
        String email = "test@example.com";
        Customer customer = getCustomer(email);

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        assertFalse(optionalCustomer.isPresent());

        customerRepository.save(customer);

        optionalCustomer = customerRepository.findByEmail(email);
        assertTrue(optionalCustomer.isPresent());

        assertEquals("test@example.com", optionalCustomer.get().getEmail());
        assertEquals("password", optionalCustomer.get().getPassword());

        assertEquals(optionalCustomer.get().getAddresses().size(), 2);
        assertNotEquals(optionalCustomer.get().getFirstName(), null);
        assertNotEquals(optionalCustomer.get().getLastName(), "Zian");
    }

    @Test
    public void testMockedUpdate() {
        String email = "test@example.com";
        Customer customer = getCustomer(email);

        when(mockedCustomerRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<Customer> foundCustomer = mockedCustomerRepository.findByEmail(email);
        assertFalse(foundCustomer.isPresent());

        when(mockedCustomerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer savedCustomer = mockedCustomerRepository.save(customer);

        assertNotNull(savedCustomer);
        assertEquals(email, savedCustomer.getEmail());
        assertEquals("password", savedCustomer.getPassword());
        assertEquals(savedCustomer.getAddresses().size(), 2);
        assertNotEquals(savedCustomer.getFirstName(), null);
        assertNotEquals(savedCustomer.getLastName(), "Zian");

        customer.setLastName("Zian");
        CustomerAddress address3 = new CustomerAddress(customer, "sidibeshr");
        customer.getAddresses().add(address3);

        Customer updatedCustomer = mockedCustomerRepository.save(customer);
        assertNotNull(updatedCustomer);
        assertEquals(email, updatedCustomer.getEmail());
        assertEquals("password", updatedCustomer.getPassword());
        assertEquals(updatedCustomer.getAddresses().size(), 3);
        assertNotEquals(updatedCustomer.getFirstName(), null);
        assertEquals(updatedCustomer.getLastName(), "Zian");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCustomerUpdate() {

        String email = "test@example.com";
        Customer customer = getCustomer(email);

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        assertFalse(optionalCustomer.isPresent());

        customerRepository.save(customer);

        optionalCustomer = customerRepository.findByEmail(email);
        assertTrue(optionalCustomer.isPresent());

        assertEquals(email, optionalCustomer.get().getEmail());
        assertEquals("password", optionalCustomer.get().getPassword());

        assertEquals(optionalCustomer.get().getAddresses().size(), 2);
        assertNotEquals(optionalCustomer.get().getFirstName(), null);
        assertNotEquals(optionalCustomer.get().getLastName(), "Zian");

        customer.setIsVerified(true);
        customer.setFirstName("omar");
        CustomerAddress address3 = new CustomerAddress(customer, "sidibeshr");
        customer.getAddresses().add(address3);
        customerRepository.save(customer);

        optionalCustomer = customerRepository.findByEmail(email);
        assertTrue(optionalCustomer.isPresent());

        assertEquals(email, optionalCustomer.get().getEmail());
        assertEquals("password", optionalCustomer.get().getPassword());

        assertEquals(optionalCustomer.get().getAddresses().size(), 3);
        assertEquals(optionalCustomer.get().getFirstName(), "omar");
        assertNotEquals(optionalCustomer.get().getLastName(), "Zian");
    }

    @Test
    public void testMockedDelete() {
        String email = "test@example.com";
        Customer customer = getCustomer(email);

        mockedCustomerRepository.delete(customer);

        // Verify that the delete method was called with the expected argument
        verify(mockedCustomerRepository).delete(customer);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testCustomerDelete() {

        String email = "test@example.com";
        Customer customer = getCustomer(email);

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        assertFalse(optionalCustomer.isPresent());

        customerRepository.save(customer);

        optionalCustomer = customerRepository.findByEmail(email);
        assertTrue(optionalCustomer.isPresent());

        assertEquals("test@example.com", optionalCustomer.get().getEmail());
        assertEquals("password", optionalCustomer.get().getPassword());

        customerRepository.delete(customer);

        optionalCustomer = customerRepository.findByEmail(email);
        assertFalse(optionalCustomer.isPresent());

        customerRepository.save(customer);
        optionalCustomer = customerRepository.findByEmail(email);
        assertTrue(optionalCustomer.isPresent());
    }
}