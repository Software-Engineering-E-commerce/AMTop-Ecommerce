package com.example.BackEnd.Controllers;
import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.WishlistElement;
import com.example.BackEnd.DTO.WishlistRequest;
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
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class WishlistControllerTest {
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
    private WishListRepository wishListRepository;

    @Autowired
    WishlistController wishlistController;
    private Customer testCustomer;

    private Product testProduct, testProduct2, testProduct3;

    private CustomerCart testCustomerCart;

    private WishList testWishlist;

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
        testProduct3.setProductCountAvailable(5);
        testProduct3.setProductSoldCount(4);
        testProduct3.setProductName("test product3 name");
        productRepository.save(testProduct3);


        testWishlist = new WishList();
        testWishlist.setCustomer(testCustomer);
        testWishlist.setProduct(testProduct);
        wishListRepository.save(testWishlist);
    }

    @Test
    @Transactional
    void testingExtractTokenAgainstValidFormat(){
        String authorizationHeader = "Bearer validToken";
        assertEquals("validToken", wishlistController.extractToken(authorizationHeader));
    }

    @Test
    @Transactional
    void testingExtractTokenAgainstInvalidFormat(){
        String authorizationHeader = "BearervalidToken";
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistController.extractToken(authorizationHeader);
        });
    }

    @Test
    @Transactional
    void testingExtractTokenAgainstBeingNull(){
        String authorizationHeader = null;
        assertThrows(IllegalArgumentException.class, () -> {
            wishlistController.extractToken(authorizationHeader);
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

    //--------------AddToWishlist tests----------------------------------------------------
    @Test
    @Transactional
    void AddingProductThatDoesntExistToWishlist_thenAddIt() throws Exception {
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct2.getId());
        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(),testProduct2.getId()));

        String expectedMessage = "Product is added successfully to the wishlist";
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(),testProduct2.getId()));

        assertEquals(2, wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
    }

    @Test
    @Transactional
    void addingProductAlready_thenThrowExceptipn() throws Exception {
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(), testProduct.getId()));
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct.getId());

        String expectedMessage = "Product's already in the wishlist !";
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(), testProduct.getId()));
        assertEquals(1, wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
    }

    @Test
    @Transactional
    void addingProductNotExist_thenThrowExceptipn() throws Exception {
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct.getId() + 10);

        String expectedMessage = "Product not exist";
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/add")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(),
                testProduct.getId()+10));
        assertEquals(1, wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
    }
    //--------------End AddToWishlist tests----------------------------------------------------

    //--------------deleteFromWishlist tests----------------------------------------------------

    @Test
    @Transactional
    void ifDeleteExistingProductInWishlist_thenAccept() throws Exception {
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(), testProduct.getId()));
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct.getId());

        String expectedMessage = "Product is deleted successfully from the wishlist";
        mockMvc.perform(MockMvcRequestBuilders.delete("/wishlist/delete")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", String.valueOf(wishlistRequest.getProductId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(),testProduct.getId()));
        assertEquals(0,wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
    }

    @Test
    @Transactional
    void ifDeleteProductNotInWishlist_thenThrowException() throws Exception {
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct2.getId());
        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId(),
                wishlistRequest.getProductId()));

        String expectedMessage = "Product is not in the wishlist";
        mockMvc.perform(MockMvcRequestBuilders.delete("/wishlist/delete")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("productId", String.valueOf(wishlistRequest.getProductId())))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId()
                , wishlistRequest.getProductId()));
        assertEquals(1,wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
    }
    //--------------End deleteFromWishlist tests----------------------------------------------------

    //--------------AddToCart tests----------------------------------------------------
    @Test
    @Transactional
    void IfAddToCartProductThatsNotInCart_thenAddIt() throws Exception {
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct.getId());
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId()
                , wishlistRequest.getProductId()));
        assertFalse(customerCartRepository.existsByCustomerAndProduct_Id(testCustomer,wishlistRequest.getProductId()));

        String expectedMessage = "Product added successfully to your cart";
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/addToCart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

        assertFalse(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId()
                , wishlistRequest.getProductId()));
        assertEquals(0,wishListRepository.findByCustomer_Id(testCustomer.getId()).size());
        assertTrue(customerCartRepository.existsByCustomerAndProduct_Id(testCustomer,wishlistRequest.getProductId()));
    }

    @Test
    @Transactional
    void IfAddToCartProductThatsInCart_thenThrowException() throws Exception {
        // This is enough to ensure that if anything goes wrong with addToCart (because AddToCart's been already tested)
        // then the database will stay as it is
        testCustomerCart = new CustomerCart();
        testCustomerCart.setCustomer(testCustomer);
        testCustomerCart.setProduct(testProduct);
        testCustomerCart.setQuantity(3);
        customerCartRepository.save(testCustomerCart);

        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(testProduct.getId());
        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId()
                , wishlistRequest.getProductId()));
        assertTrue(customerCartRepository.existsByCustomerAndProduct_Id(testCustomer,wishlistRequest.getProductId()));

        String expectedMessage = "Product's already in the cart";
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/addToCart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(wishlistRequest)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

        assertTrue(wishListRepository.existsByCustomer_IdAndProduct_Id(testCustomer.getId()
                , wishlistRequest.getProductId()));
        assertTrue(customerCartRepository.existsByCustomerAndProduct_Id(testCustomer,wishlistRequest.getProductId()));
    }
    //--------------End AddToCart tests----------------------------------------------------

    //--------------getWishlistElements tests----------------------------------------------------
    @Test
    @Transactional
    void testGetWishlistElementsEmpty() throws Exception {
        // Delete the wishlist entry to make the wishlist empty
        wishListRepository.deleteByCustomerAndProduct_Id(testCustomer, testProduct.getId());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wishlist/getWishlistElements")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<WishlistElement> wishlistElements = convertJsonToWishlistElementList(
                result.getResponse().getContentAsString());

        // Add assertions to verify that wishlist is empty
        assertEquals(0, wishlistElements.size());
    }


    @Test
    @Transactional
    void getWishlistElementsMoreThanOne() throws Exception {
        //adding two more products to the customer wishlist
        WishList testWishlist2 = new WishList();
        testWishlist2.setCustomer(testCustomer);
        testWishlist2.setProduct(testProduct2);
        wishListRepository.save(testWishlist2);

        WishList testWishlist3 = new WishList();
        testWishlist3.setCustomer(testCustomer);
        testWishlist3.setProduct(testProduct3);
        wishListRepository.save(testWishlist3);

        assertEquals(3, wishListRepository.findByCustomer_Id(testCustomer.getId()).size());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/wishlist/getWishlistElements")
                        .header("Authorization", "Bearer " + token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<WishlistElement> wishlistElements = convertJsonToWishlistElementList(
                result.getResponse().getContentAsString());

        // Now let's verify that the wishlist has 3 elements in it
        assertEquals(3, wishlistElements.size());
    }


    // Helper method to convert JSON string to a list of wishlistElement objects
    private List<WishlistElement> convertJsonToWishlistElementList(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, new TypeReference<List<WishlistElement>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to list of WishlistElement", e);
        }
    }
    //--------------End getWishlistElements tests----------------------------------------------------

}