package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.DTO.HomeInfo;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CategoryRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private HomeService homeService;

    @BeforeEach
    void setUp(){
        String mockToken = "sd2151ewf";
        String mockUserName = "MahmoudAli";
        when(jwtService.extractUsername(mockToken)).thenReturn(mockUserName);
    }

    //--------getCustomerTests-------------------------------------------------------
    @Test
    void testCustomerExistence(){
        String mockToken = "sd2151ewf";
        String mockUserName = "MahmoudAli";
        Customer c = new Customer();

        when(customerRepository.findByEmail(mockUserName)).thenReturn(Optional.of(c));

        assertEquals(c,homeService.getCustomer(mockToken));
    }

    @Test
    void ifCustomerNotExistThenWeShouldGetNull(){
        String mockToken = "sd2151ewf";
        String mockUserName = "MahmoudAli";


        Optional<Customer> c = customerRepository.findByEmail("Mahmoud@example.com");

        assertNull(homeService.getCustomer(mockToken));
    }
    //--------End getCustomerTests--------------------------------------------------------


    //--------getAdminTests-------------------------------------------------------
    @Test
    void testAdminExistence(){
        String mockToken = "sd2151ewf";
        String mockUserName = "MahmoudAli";
        Admin a = new Admin();

        when(adminRepository.findByEmail(mockUserName)).thenReturn(Optional.of(a));

        assertEquals(a,homeService.getAdmin(mockToken));
    }

    @Test
    void ifAdminNotExistThenWeShouldGetNull(){
        String mockToken = "sd2151ewf";
        String mockUserName = "MahmoudAli";

        Optional<Admin> a = adminRepository.findByEmail("Mahmoud@example.com");

        assertNull(homeService.getAdmin(mockToken));
    }
    //--------End getAdminTests--------------------------------------------------------

    //--------getCategoriesTests-------------------------------------------------------
    @Test
    void getCategoriesTest(){
        List<Category> categoryList=categoryRepository.findAll();
        assertNotNull(categoryList);
    }
    //--------End getCategoriesTests--------------------------------------------------------

    //--------getHomeInfoTests-------------------------------------------------------

    // Helper method to create a mock Category
    private Category createMockCategories(String categoryName, String categoryImageLink) {
        Category mockCategories = mock(Category.class);

        when(mockCategories.getCategoryName()).thenReturn(categoryName);
        when(mockCategories.getImageLink()).thenReturn(categoryImageLink);
        return mockCategories;
    }

    @Test
    void getAllCategoriesTest() {
        // Mock data
        String mockToken = "sd2151ewf";

        // Mock the categories elements
        List<Category> mockCategoryList = Arrays.asList(
                createMockCategories("All Categories","www.AllCategories.png"),
                createMockCategories("Labs","www.Labs.png"),
                createMockCategories("Processors","www.Processors.png")
        );
        when(categoryRepository.findAll()).thenReturn(mockCategoryList);

        // Call the method to be tested
        List<Category> result2 = homeService.getCategories();

        //assert results
        assertEquals(3,result2.size());
    }
    @Test
    void getHomeInfoTestForCustomer(){
        // Mock data
        String mockToken = "sd2151ewf";
        Long customerId = 1L;
        Customer mockCustomer = mock(Customer.class);

        // Mock the repository behavior
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(mockCustomer));
        when(mockCustomer.getId()).thenReturn(customerId);

        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Mock the categories elements
        List<Category> mockCategoryList = Arrays.asList(
                createMockCategories("All Categories","www.AllCategories.png"),
                createMockCategories("Labs","www.Labs.png"),
                createMockCategories("Processors","www.Processors.png")
        );
        when(categoryRepository.findAll()).thenReturn(mockCategoryList);

        HomeInfo res=homeService.getHomeInfo(mockToken);
        HomeInfo expected=new HomeInfo(mockCustomer.getFirstName(),mockCustomer.getLastName(),false,mockCategoryList);
        assertEquals(expected,res);
    }
    @Test
    void getHomeInfoTestForAdmin(){
        // Mock data
        String mockToken = "sd2151ewf";
        Long adminId = 1L;
        Admin mockAdmin = mock(Admin.class);

        // Mock the repository behavior
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(mockAdmin));
        when(mockAdmin.getId()).thenReturn(adminId);

        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Mock the categories elements
        List<Category> mockCategoryList = Arrays.asList(
                createMockCategories("All Categories","www.AllCategories.png"),
                createMockCategories("Labs","www.Labs.png"),
                createMockCategories("Processors","www.Processors.png")
        );
        when(categoryRepository.findAll()).thenReturn(mockCategoryList);

        HomeInfo res=homeService.getHomeInfo(mockToken);
        HomeInfo expected=new HomeInfo(mockAdmin.getFirstName(),mockAdmin.getLastName(),true,mockCategoryList);
        assertEquals(expected,res);
    }
    @Test
    void getNullHomeInfoTest() {
        // Mock data
        String mockToken = "sd2151ewf";

        // Mock the repository behavior
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty()); // Return an empty Optional

        // Mock the categories elements
        List<Category> mockCategoryList = Arrays.asList(
                createMockCategories("All Categories", "www.AllCategories.png"),
                createMockCategories("Labs", "www.Labs.png"),
                createMockCategories("Processors", "www.Processors.png")
        );
        when(categoryRepository.findAll()).thenReturn(mockCategoryList);

        // Perform the test
        HomeInfo res = homeService.getHomeInfo(mockToken);

        // Assert that the result is null
        assertNull(res);
    }

    //--------End getHomeInfoTests--------------------------------------------------------

}
