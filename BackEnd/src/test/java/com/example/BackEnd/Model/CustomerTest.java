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

        //simulate the create customer by the mocked repo
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

        //test the create customer by checking it before adding and after adding
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

        //simulate the update operations by using the mocked repo
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

        //test the update customer by checking it after and before update
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

        //test the delete customer by adding then deleting the add again
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

    @Test
    public void testConstructorAndGetters() {
        // Set up
        String email = "customer@example.com";
        String password = "password";
        boolean isGmail = true;
        boolean isVerified = true;
        String firstName = "John";
        String lastName = "Doe";

        // Create customer using constructor
        Customer customer = new Customer(email, password, isGmail, isVerified, firstName, lastName);

        // Verify
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertTrue(customer.getIsGmail());
        assertTrue(customer.getIsVerified());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
    }

    @Test
    public void testSettersAndGetters() {
        // Set up
        Customer customer = new Customer();
        String email = "newcustomer@example.com";
        String password = "newpassword";
        boolean isGmail = false;
        boolean isVerified = false;
        String firstName = "Jane";
        String lastName = "Doe";

        // Set values using setters
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setIsGmail(isGmail);
        customer.setIsVerified(isVerified);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);

        // Verify
        assertEquals(email, customer.getEmail());
        assertEquals(password, customer.getPassword());
        assertFalse(customer.getIsGmail());
        assertFalse(customer.getIsVerified());
        assertEquals(firstName, customer.getFirstName());
        assertEquals(lastName, customer.getLastName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Set up
        Customer customer1 = new Customer();
        customer1.setId(1L);
        customer1.setEmail("customer@example.com");

        Customer customer2 = new Customer();
        customer2.setId(1L);
        customer2.setEmail("customer@example.com");

        Customer customer3 = new Customer();
        customer3.setId(2L);
        customer3.setEmail("different@example.com");

        // Mocking repository behavior
        when(mockedCustomerRepository.findById(1L)).thenReturn(Optional.of(customer1));
        when(mockedCustomerRepository.findById(2L)).thenReturn(Optional.of(customer3));

        // Verify
        assertEquals(customer1, customer2);
        assertNotEquals(customer1, customer3);
        assertEquals(customer1.hashCode(), customer2.hashCode());
        assertNotEquals(customer1.hashCode(), customer3.hashCode());
    }

    @Test
    public void testAddAddress() {
        // Set up
        Customer customer = new Customer();
        CustomerAddress address = new CustomerAddress();

        // Call the method
        customer.getAddresses().add(address);

        // Verify
        assertTrue(customer.getAddresses().contains(address));
        assertEquals(1, customer.getAddresses().size());
    }

    @Test
    public void testRemoveAddress() {
        // Set up
        Customer customer = new Customer();
        CustomerAddress address = new CustomerAddress();
        customer.getAddresses().add(address);

        // Call the method
        customer.getAddresses().remove(address);

        // Verify
        assertFalse(customer.getAddresses().contains(address));
        assertEquals(0, customer.getAddresses().size());
    }
}