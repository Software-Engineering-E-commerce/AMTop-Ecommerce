package com.example.BackEnd.Controllers;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.DTO.CartRequest;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerCartRepository customerCartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartController cartController;

    private Customer testCustomer;

    private Product testProduct, testProduct2, testProduct3;

    private CustomerCart testCustomerCart;

    private Category testCategory;
    private String token;

    // Note here this is executed before each @Test method but thanks to @Transactional annotation it ensures
    // That any transaction that has occured in this test is rolled back as nothing's ever happened
    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setEmail("testcustomer@example.com");
        testCustomer.setPassword("somePassword");
        testCustomer.setFirstName("testFirstName");
        testCustomer.setLastName("testLastName");
        testCustomer.setIsGmail(false);
        testCustomer.setIsVerified(true);
        customerRepository.save(testCustomer);
        token = jwtService.generateToken(testCustomer);
        System.out.println("Token: " + token);

        testCategory = new Category();
        testCategory.setCategoryName("test category name");
        testCategory.setImageLink("test category image link");
        categoryRepository.save(testCategory);

        testProduct = new Product();
        testProduct.setBrand("testBrand");
        testProduct.setPrice((float)18.5);
        testProduct.setDescription("test description");
        testProduct.setImageLink("Image link");
        testProduct.setDiscountPercentage(0);
        testProduct.setCategory(testCategory);
        testProduct.setPostedDate(LocalDateTime.now());
        testProduct.setProductCountAvailable(10);
        testProduct.setProductSoldCount(2);
        testProduct.setProductName("test product name");
        productRepository.save(testProduct);


        testProduct2 = new Product();
        testProduct2.setBrand("testBrand2");
        testProduct2.setPrice((float)18.45);
        testProduct2.setDescription("test description2");
        testProduct2.setImageLink("Image link2");
        testProduct2.setDiscountPercentage(0);
        testProduct2.setCategory(testCategory);
        testProduct2.setPostedDate(LocalDateTime.now());
        testProduct2.setProductCountAvailable(20);
        testProduct2.setProductSoldCount(4);
        testProduct2.setProductName("test product2 name");
        productRepository.save(testProduct2);

        testProduct3 = new Product();
        testProduct3.setBrand("testBrand3");
        testProduct3.setPrice((float)18.45);
        testProduct3.setDescription("test description3");
        testProduct3.setImageLink("Image link3");
        testProduct3.setDiscountPercentage(0);
        testProduct3.setCategory(testCategory);
        testProduct3.setPostedDate(LocalDateTime.now());
        testProduct3.setProductCountAvailable(0);
        testProduct3.setProductSoldCount(4);
        testProduct3.setProductName("test product3 name");
        productRepository.save(testProduct3);

        testCustomerCart = new CustomerCart();
        testCustomerCart.setCustomer(testCustomer);
        testCustomerCart.setProduct(testProduct);
        testCustomerCart.setQuantity(3);
        customerCartRepository.save(testCustomerCart);
    }


    @Test
    @Transactional
    void testingExtractTokenAgainstValidFormat(){
        String authorizationHeader = "Bearer validToken";
        assertEquals("validToken", cartController.extractToken(authorizationHeader));
    }

    @Test
    @Transactional
    void testingExtractTokenAgainstInvalidFormat(){
        String authorizationHeader = "BearervalidToken";
        assertThrows(IllegalArgumentException.class, () -> {
            cartController.extractToken(authorizationHeader);
        });
    }

    @Test
    @Transactional
    void testingExtractTokenAgainstBeingNull(){
        String authorizationHeader = null;
        assertThrows(IllegalArgumentException.class, () -> {
            cartController.extractToken(authorizationHeader);
        });
    }

    // Helper method to convert objects to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //--------addToCart Tests-------------------------------

    @Test
    @Transactional
    void testHappyCaseAdd() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct2.getId());

        String expectedMessage = "Product is added successfully to the cart";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

        assertEquals(1, customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct2.getId()).get().getQuantity());
    }

    @Test
    @Transactional
    void addingProductAlreadyInCartTest() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());

        String expectedMessage = "Product's already in the cart";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertTrue(customerCartRepository.findByCustomerAndProduct_Id(testCustomer, testProduct.getId()).isPresent());
    }

    @Test
    @Transactional
    void addingProductThatDosntExist() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct2.getId() + 5);

        String expectedMessage = "Product not exist";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(customerCartRepository.findByCustomerAndProduct_Id(testCustomer, testProduct2.getId()+5).isPresent());
    }

    @Test
    @Transactional
    void productOutOfStock() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct3.getId());

        String expectedMessage = "Product out of stock";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(customerCartRepository.findByCustomerAndProduct_Id(testCustomer, testProduct3.getId()).isPresent());
    }

    //--------End addToCart Tests----------------------------------------------------------

    //--------setQuantity Tests----------------------------------------------------------
    @Test
    @Transactional
    void happyScenarioSet() throws Exception {
        //setting quantity for a product in my cart with quantity < available
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());

        assertTrue(customerCartRepository.findByCustomerAndProduct_Id(testCustomer, testProduct.getId()).isPresent());
        String expectedMessage = "Quantity is set successfully for this product";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/setQuantity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", "5")
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertEquals(5,customerCartRepository.findByCustomerAndProduct_Id(testCustomer, testProduct.getId()).get().getQuantity());
    }

    @Test
    @Transactional
    void AddingExceedingQuantity() throws Exception {
        //setting quantity for a product in my cart with quantity < available
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());
        int oldQunatity = customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct.getId()).get().getQuantity();
        String expectedMessage = "Sorry, the requested quantity exceeds the available count for this product";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/setQuantity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", "11")
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertEquals(oldQunatity,customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct.getId()).get().getQuantity());
    }

    //--------End setQuantity tests-----------------------------------------------------

    //--------deleteFromCart Tests----------------------------------------------------------
    @Test
    @Transactional
    void happyScenarioDelete() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());
        String expectedMessage = "Product is deleted successfully from the cart";
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", cartRequest.getProductId().toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct.getId()).isPresent());
    }
    @Test
    @Transactional
    void deleteProductNotInCart() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct2.getId());

        assertFalse(customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct2.getId()).isPresent());
        String expectedMessage = "Product is not in the cart";
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", cartRequest.getProductId().toString()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @Transactional
    void settingQuantityTo0() throws Exception {
        //setting quantity for a product in my cart with quantity < available
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());

        String expectedMessage = "Product is deleted successfully from the cart";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/setQuantity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", "0")
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(customerCartRepository.findByCustomerAndProduct_Id(testCustomer,testProduct2.getId()).isPresent());
    }
    //--------End deleteFromCart Tests----------------------------------------------------------

    //-------getCartElements Tests--------------------------------------------------------------
    @Test
    @Transactional
    void getCartElementsMoreThanOne() throws Exception {
        //adding two more products to the customer cart
        CustomerCart testCustomerCart2 = new CustomerCart();
        testCustomerCart2.setCustomer(testCustomer);
        testCustomerCart2.setProduct(testProduct2);
        testCustomerCart2.setQuantity(5);
        customerCartRepository.save(testCustomerCart2);

        testProduct3.setProductCountAvailable(5);

        CustomerCart testCustomerCart3 = new CustomerCart();
        testCustomerCart3.setCustomer(testCustomer);
        testCustomerCart3.setProduct(testProduct3);
        testCustomerCart3.setQuantity(3);
        customerCartRepository.save(testCustomerCart3);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/getCartElements")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<CartElement> cartElements = convertJsonToCartElementList(
                result.getResponse().getContentAsString());

        assertEquals(3, cartElements.size());

    }

    @Test
    @Transactional
    void testGetCartElementsEmpty() throws Exception {
        // Delete the cart entry to make the cart empty
        customerCartRepository.deleteByCustomerAndProduct_Id(testCustomer, testProduct.getId());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/cart/getCartElements")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<CartElement> cartElements = convertJsonToCartElementList(
                result.getResponse().getContentAsString());

        // Add assertions to verify that cartElements is empty
        assertEquals(0, cartElements.size());
    }


    // Helper method to convert JSON string to a list of CartElement objects
    private List<CartElement> convertJsonToCartElementList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<CartElement>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to list of CartElement", e);
        }
    }
    //-------End getCartElements Tests--------------------------------------------------------------

    //-------Checkout tests------------------------------------------------------------------------
    @Test
    @Transactional
    void successFulCheckout() throws Exception {
        // adding test product two as well
        CustomerCart testCustomerCart2 = new CustomerCart();
        testCustomerCart2.setCustomer(testCustomer);
        testCustomerCart2.setProduct(testProduct2);
        testCustomerCart2.setQuantity(4);
        customerCartRepository.save(testCustomerCart2);
        assertEquals(2,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());
        // Now we have both testProduct and testProduct2 in cart with quantity 3, 4 respectively

        int quantity1 = customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct.getId()).get().getQuantity();
        int quantity2 = customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct2.getId()).get().getQuantity();

        int beforeAvailability1 =  customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct.getId()).get().getProduct().getProductCountAvailable();
        int beforeAvailability2 =  customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct2.getId()).get().getProduct().getProductCountAvailable();

        int beforeSoldCount1 = customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct.getId()).get().getProduct().getProductSoldCount();

        int beforeSoldCount2 = customerCartRepository.findByCustomerAndProduct_Id(testCustomer
                , testProduct2.getId()).get().getProduct().getProductSoldCount();

        // adding an address to our customer for successful checkout
        CustomerAddress customerAddress = new CustomerAddress(testCustomer,"123 main street");
        testCustomer.setAddresses(Arrays.asList(customerAddress));

        String expectedMessage = "Order has been placed successfully !";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/checkout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage))
                .andReturn();

        // at this point the order is built successfully so let's check our tables
        int afterAvailability1 = testProduct.getProductCountAvailable();
        int afterAvailability2 = testProduct2.getProductCountAvailable();
        int afterSoldCount1 = testProduct.getProductSoldCount();
        int afterSoldCount2 = testProduct2.getProductSoldCount();

        assertEquals(beforeAvailability1 - quantity1, afterAvailability1);
        assertEquals(beforeAvailability2 - quantity2, afterAvailability2);
        assertEquals(beforeSoldCount1 + quantity1, afterSoldCount1);
        assertEquals(beforeSoldCount2 + quantity2, afterSoldCount2);
        assertEquals(1, orderRepository.findByCustomer(testCustomer).size());
        assertEquals(2, orderItemRepository.findByOrder(orderRepository.findByCustomer(testCustomer).get(0)).size());
        assertEquals(0,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());
    }

    @Test
    @Transactional
    void addressNotProvidedCheckout_thenThrowAnException() throws Exception {
        // adding test product two as well
        CustomerCart testCustomerCart2 = new CustomerCart();
        testCustomerCart2.setCustomer(testCustomer);
        testCustomerCart2.setProduct(testProduct2);
        testCustomerCart2.setQuantity(4);
        customerCartRepository.save(testCustomerCart2);
        assertEquals(2,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());

        //our customer has no address let's check what happens when he places an order
        String expectedMessage = "No address provided yet !";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/checkout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage))
                .andReturn();

        // at this point the order is built successfully so let's check our tables
        assertEquals(0, orderRepository.findByCustomer(testCustomer).size());
        assertEquals(2,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());
    }

    @Test
    @Transactional
    void customerCartEmpty_thenThrowException() throws Exception {
        // Delete the testCustomer
        customerCartRepository.deleteByCustomer(testCustomer);
        assertEquals(0,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());

        // Our customer has no address let's check what happens when he places an order
        String expectedMessage = "Can't happen, your cart is empty !";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/checkout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage))
                .andReturn();

        // At this point the order is built successfully so let's check our tables
        assertEquals(0, orderRepository.findByCustomer(testCustomer).size());
    }

    @Test
    @Transactional
    void ifOneProductHasQuantityGreaterThanWhatsAvailable_thenThrowException() throws Exception {

        // Adding test product two as well
        CustomerCart testCustomerCart2 = new CustomerCart();
        testCustomerCart2.setCustomer(testCustomer);
        testCustomerCart2.setProduct(testProduct2);
        testCustomerCart2.setQuantity(21); //greater than what's available of this product
        customerCartRepository.save(testCustomerCart2);
        assertEquals(2,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());

        // Adding an address to our customer for successful checkout
        CustomerAddress customerAddress = new CustomerAddress(testCustomer,"123 main street");
        testCustomer.setAddresses(Arrays.asList(customerAddress));

        String expectedMessage = "The available in stock right now for test product2 name product is only 20";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/checkout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage))
                .andReturn();
        // At this point the order is built successfully so let's check our tables
        assertEquals(0, orderRepository.findByCustomer(testCustomer).size());
        assertEquals(2,customerCartRepository.findByCustomer_Id(testCustomer.getId()).size());
    }
    //-------End Checkout tests---------------------------------------------------------------------
}