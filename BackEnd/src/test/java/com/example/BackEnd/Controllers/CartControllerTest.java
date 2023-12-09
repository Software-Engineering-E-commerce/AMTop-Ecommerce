package com.example.BackEnd.Controllers;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.CartElement;
import com.example.BackEnd.DTO.CartRequest;
import com.example.BackEnd.Model.Category;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Model.CustomerCart;
import com.example.BackEnd.Model.Product;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerCartRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.example.BackEnd.Repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
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
    private CartController cartController;

    private Customer testCustomer;

    private Product testProduct, testProduct2, testProduct3;

    private CustomerCart testCustomerCart;

    private Category testCategory;
    private String token;

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

    @AfterEach
    void tearDown(){
        // Delete child records first
        customerCartRepository.delete(testCustomerCart);

        // Delete parent records
        productRepository.delete(testProduct);
        productRepository.delete(testProduct2);
        productRepository.delete(testProduct3);
        categoryRepository.delete(testCategory);
        customerRepository.delete(testCustomer);
    }

    @Test
    void testingExtractTokenAgainstValidFormat(){
        String authorizationHeader = "Bearer validToken";
        assertEquals("validToken", cartController.extractToken(authorizationHeader));
    }

    @Test
    void testingExtractTokenAgainstInvalidFormat(){
        String authorizationHeader = "BearervalidToken";
        assertThrows(IllegalArgumentException.class, () -> {
           cartController.extractToken(authorizationHeader);
        });
    }

    @Test
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

        //then we delete back the tuple that's been added
        customerCartRepository.deleteByCustomerAndProduct_Id(testCustomer, testProduct2.getId());
    }

    @Test
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
    }

    @Test
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
    }

    @Test
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
    }

    //--------End addToCart Tests----------------------------------------------------------

    //--------setQuantity Tests----------------------------------------------------------
    @Test
    void happyScenarioSet() throws Exception {
        //setting quantity for a product in my cart with quantity < available
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());

        String expectedMessage = "Quantity is set successfully for this product";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/setQuantity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", "5")
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    void AddingExceedingQuantity() throws Exception {
        //setting quantity for a product in my cart with quantity < available
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct.getId());

        String expectedMessage = "Sorry, the requested quantity exceeds the available count for this product";
        mockMvc.perform(MockMvcRequestBuilders.post("/cart/setQuantity")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("quantity", "11")
                        .content(asJsonString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    //--------End setQuantity tests-----------------------------------------------------

    //--------deleteFromCart Tests----------------------------------------------------------
    @Test
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
    }
    @Test
    void deleteProductNotInCart() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setProductId(testProduct2.getId());

        String expectedMessage = "Product is not in the cart";
        mockMvc.perform(MockMvcRequestBuilders.delete("/cart/delete")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", cartRequest.getProductId().toString()))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
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

        customerCartRepository.delete(testCustomerCart2);
        customerCartRepository.delete(testCustomerCart3);
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

}