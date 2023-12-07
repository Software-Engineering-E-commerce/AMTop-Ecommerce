package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class CartServiceTest {

    @Mock
    private  CustomerCartRepository customerCartRepository;
    @Mock
    private  CustomerRepository customerRepository;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  JwtService jwtService;

    @Mock
    private  CustomerCart customerCart;
    @Mock
    private Product product;

    @InjectMocks
    private CartService cartService;


    @BeforeEach
    void setUp(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        when(jwtService.extractUsername(mockToken)).thenReturn(mockUserName);
    }

    //--------getCustomerTests-------------------------------------------------------
    @Test
    void testCustomerExistence(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Customer c = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(c));

        assertEquals(c,cartService.getCustomer(mockToken));
    }

    @Test
    void ifCustomerNotExistThenWeShouldGetNull(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";


        Optional<Customer> c = customerRepository.findByEmail("Adel@example.com");

        assertNull(cartService.getCustomer(mockToken));
    }
    //--------End getCustomerTests--------------------------------------------------------


    //-------addToCart Tests--------------------------------------------------------------
    @Test
    void happyScenarioAdd() throws IllegalAccessException {
        //meaning that our customer exists, product is not in the cart, there's available items of the product
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();


        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(false);
        when(productRepository.findProductById(mockProductId)).thenReturn(Optional.of(product));
        when(product.getProductCountAvailable()).thenReturn(5);

        // When

        cartService.addToCart(mockToken,mockProductId);

        // Then
        // Verify that save method was called with the correct arguments
        verify(customerCartRepository).save(argThat(customerCart ->
                customerCart.getCustomer().equals(mockCustomer) &&
                        customerCart.getProduct().equals(product) &&
                        customerCart.getQuantity() == 1));
    }

    @Test
    void AddingProductAlreadyInCart(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();


        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.addToCart(mockToken,mockProductId));
        assertEquals(exception.getMessage(), "Product's already in the cart");
    }

    @Test
    void AddingProductDoesntExistToCart(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();


        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(false);
        when(productRepository.findProductById(mockProductId)).thenReturn(Optional.empty());

        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> cartService.addToCart(mockToken,mockProductId));
        assertEquals(exception.getMessage(), "Product not exist");
    }

    @Test
    void AddingOutOfStockProduct(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();


        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(false);
        when(productRepository.findProductById(mockProductId)).thenReturn(Optional.of(product));
        when(product.getProductCountAvailable()).thenReturn(0);

        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> cartService.addToCart(mockToken,mockProductId));
        assertEquals(exception.getMessage(), "Product out of stock");
    }

    @Test
    void UserNotFound(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                cartService.addToCart(mockToken,mockProductId));
        assertEquals(exception.getMessage(), "Customer is not found");
    }

    //------End addToCart Tests----------------------------------------------------------

    //-------setQuantity Tests--------------------------------------------------------------
    @Test
    void happyScenarioSet(){
        //now the product is in the cart, quantity < available count
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        int mockQauntity = 5;
        Customer mockCustomer = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(true);
        when(productRepository.findProductById(mockProductId)).thenReturn(Optional.of(product));
        when(product.getProductCountAvailable()).thenReturn(6);
        when(customerCartRepository.findByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(Optional.of(customerCart));
        when(customerCart.getCustomer()).thenReturn(mockCustomer);
        when(customerCart.getProduct()).thenReturn(product);
        when(customerCart.getQuantity()).thenReturn(mockQauntity);

        //when
        cartService.setQuantity(mockToken,mockProductId,mockQauntity);

        // Verify that the customerCartRepository.save method is called once with the correct arguments
        verify(customerCartRepository, times(1)).save(argThat(savedCart ->
                savedCart.getCustomer().equals(mockCustomer) &&
                        savedCart.getProduct().equals(product) &&
                        savedCart.getQuantity() == mockQauntity
        ));
    }

    @Test
    void AddingExceedingQuantity(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        int mockQauntity = 5;
        Customer mockCustomer = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(true);
        when(productRepository.findProductById(mockProductId)).thenReturn(Optional.of(product));
        when(product.getProductCountAvailable()).thenReturn(4);

        IllegalStateException exception = assertThrows(IllegalStateException.class , () -> {
           cartService.setQuantity(mockToken,mockProductId,mockQauntity);
        });
        assertEquals("Sorry, the requested quantity exceeds the available count for this product" , exception.getMessage());
    }

    @Test
    void settingQunatityForProductNotInCart(){
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        int mockQauntity = 5;
        Customer mockCustomer = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(false);
        IllegalStateException exception = assertThrows(IllegalStateException.class , () -> {
            cartService.setQuantity(mockToken,mockProductId,mockQauntity);
        });
        assertEquals("Product is not in the cart" , exception.getMessage());
    }
    //-------End setQuantity Tests--------------------------------------------------------------


}