package com.example.BackEnd.Services;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;
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


    //-------deleteFromCart Tests--------------------------------------------------------------
    @Test
    void happyScenarioDelete(){
        //deleting a product that exists in my cart
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(true);

        //when
        cartService.deleteFromCart(mockToken, mockProductId);

        // Verify that deleteByCustomerAndProduct_Id is called with the correct arguments
        verify(customerCartRepository).deleteByCustomerAndProduct_Id(mockCustomer, mockProductId);
    }

    @Test
    void deleteProductNotInCart(){
        //deleting a product that exists in my cart
        String mockToken = "sd2151ewf";
        String mockUserName = "AdelMahmoud";
        Long mockProductId = 14L;
        Customer mockCustomer = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(mockCustomer));
        when(customerCartRepository.existsByCustomerAndProduct_Id(mockCustomer,mockProductId)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
           cartService.deleteFromCart(mockToken,mockProductId);
        });

        assertEquals("Product is not in the cart", exception.getMessage());
    }
    //-------End deleteFromCart Tests--------------------------------------------------------------


    //-------getCartElements Tests--------------------------------------------------------------
    @Test
    void convertToCartElementTest() {
        // Mock data
        String mockToken = "sd2151ewf";
        CustomerCart mockCustomerCart = mock(CustomerCart.class);
        Product mockProduct = mock(Product.class);

        // Set up expectations for the mocks
        when(mockCustomerCart.getProduct()).thenReturn(mockProduct);
        when(mockProduct.getId()).thenReturn(1L);
        when(mockProduct.getProductName()).thenReturn("Mock Product");
        when(mockProduct.getPrice()).thenReturn(20.0f);
        when(mockProduct.getDescription()).thenReturn("Mock Description");
        when(mockProduct.getImageLink()).thenReturn("mock_image_link");
        when(mockCustomerCart.getQuantity()).thenReturn(3);
        when(mockCustomerCart.getProduct().getDiscountPercentage()).thenReturn((float)1.2);

        // Call the method to be tested
        CartElement result = cartService.convertToCartElement(mockCustomerCart, mockToken);

        // Verify the result
        assertEquals(1L, result.getId());
        assertEquals("Mock Product", result.getProductName());
        assertEquals(20.0f, result.getPrice());
        assertEquals("Mock Description", result.getDescription());
        assertEquals("mock_image_link", result.getImageLink());
        assertEquals(3, result.getQuantity());
        assertEquals("sd2151ewf", result.getToken());
        assertEquals((float)1.2, result.getDiscountPercentage());
    }

    // Helper method to create a mock CustomerCart
    private CustomerCart createMockCustomerCart(Customer customer, Long productId, int quantity) {
        CustomerCart mockCustomerCart = mock(CustomerCart.class);
        Product mockProduct = mock(Product.class);

        when(mockCustomerCart.getCustomer()).thenReturn(customer);
        when(mockCustomerCart.getProduct()).thenReturn(mockProduct);
        when(mockProduct.getId()).thenReturn(productId);
        when(mockCustomerCart.getQuantity()).thenReturn(quantity);

        return mockCustomerCart;
    }
    @Test
    void getAllCartElementsForCustomerTest() {
        // Mock data
        String mockToken = "sd2151ewf";
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);

        // Mock the repository behavior
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(mockCustomer));
        when(mockCustomer.getId()).thenReturn(customerId);

        // Mock the cart elements
        List<CustomerCart> mockCustomerCartList = Arrays.asList(
                createMockCustomerCart(mockCustomer, 1L, 2),
                createMockCustomerCart(mockCustomer, 3L, 4)
        );
        when(customerCartRepository.findByCustomer_Id(customerId)).thenReturn(mockCustomerCartList);

        // Call the method to be tested
        List<CartElement> result = cartService.getCartElements(mockToken);

        // Verify the result
        assertEquals(2, result.size()); // Adjust based on your mock data
        // Add more assertions based on the expected behavior of your service
    }
    //-------End getCartElements Tests--------------------------------------------------------------

}

