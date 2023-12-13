package com.example.BackEnd.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.example.BackEnd.DTO.UserProfileDTO;
import com.example.BackEnd.Model.*;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerAddressRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProfileServiceTest {

    @InjectMocks
    private ProfileService profileService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerAddressRepository customerAddressRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void retrieveCustomerData_ShouldReturnCustomerProfileDTO() {
        String email = "customer@example.com";
        Customer mockCustomer = createMockCustomer();
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));

        UserProfileDTO result = profileService.retrieveData(email);

        // Assert
        assertNotNull(result);
        assertTrue(result.getIsCustomer());
        assertEquals(mockCustomer.getFirstName(), result.getFirstName());
        assertEquals(mockCustomer.getLastName(), result.getLastName());
        assertEquals(mockCustomer.getPhoneNumber(), result.getPhoneNumber());

        List<String> addresses = new ArrayList<>();
        for(CustomerAddress address: mockCustomer.getAddresses()){
            addresses.add(address.getAddress());
        }
        assertEquals(addresses, result.getAddresses());
        assertNull(result.getContactPhone());

        // verify that findByEmail method of the customerRepository mock object was called exactly once
        // with the specified email
        verify(customerRepository).findByEmail(email);
        // verify that findByEmail method of the customerRepository mock object was never called
        // with the specified email
        verify(adminRepository, never()).findByEmail(email);
    }

    @Test
    public void retrieveAdminData_ShouldReturnAdminProfileDTO() {
        String email = "admin@example.com";
        Admin mockAdmin = createMockAdmin();
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(mockAdmin));

        UserProfileDTO result = profileService.retrieveData(email);

        // Assert
        assertNotNull(result);
        assertFalse(result.getIsCustomer());
        assertEquals(mockAdmin.getFirstName(), result.getFirstName());
        assertEquals(mockAdmin.getLastName(), result.getLastName());
        assertEquals(mockAdmin.getAddress(), result.getAddresses().get(0)); // Assuming addresses are set correctly
        assertEquals(mockAdmin.getContactPhone(), result.getContactPhone());
        assertEquals(mockAdmin.getPhoneNumber(), result.getPhoneNumber());
        // Verify behavior
        verify(customerRepository).findByEmail(email);
        verify(adminRepository).findByEmail(email);
    }

    @Test
    public void retrieveData_ShouldReturnNull_WhenEmailNotFound() {
        String email = "notfound@example.com";
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        UserProfileDTO result = profileService.retrieveData(email);

        // Assert
        assertNull(result);

        // Verify behavior
        verify(customerRepository).findByEmail(email);
        verify(adminRepository).findByEmail(email);
    }

    private Customer createMockCustomer() {
        Customer mockCustomer = new Customer("customer@example.com", "password",
                true, true, "John", "Doe");
        mockCustomer.setId(1L);
        mockCustomer.setPhoneNumber("1234567890");
        List<CustomerAddress> customerAddresses = new ArrayList<>();
        String[] dummyAdrress = {"1st address", "2nd address", "3rd address"};
        for(String address: dummyAdrress){
            CustomerAddressPK customerAddressPK = new CustomerAddressPK(mockCustomer, address);
            if(!customerAddressRepository.findById(customerAddressPK).isPresent()){
                customerAddresses.add(new CustomerAddress(mockCustomer, address));
            }
        }
        mockCustomer.setAddresses(customerAddresses);
        return mockCustomer;
    }
    private Admin createMockAdmin() {
        Admin mockAdmin = new Admin();
        mockAdmin.setId(1L);
        mockAdmin.setFirstName("Alice");
        mockAdmin.setLastName("Smith");
        mockAdmin.setEmail("admin@example.com");
        mockAdmin.setAddress("123 Admin St");
        mockAdmin.setContactPhone("123-456-7890");
        mockAdmin.setPhoneNumber("1234567890");
        return mockAdmin;
    }
    // updateData tests
    @Test
    public void updateData_ShouldUpdateCustomer_WhenEmailBelongsToCustomer() {
        String email = "customer@example.com";
        UserProfileDTO dto = createMockUserProfileDTO();
        Customer mockCustomer = createMockCustomer();
        when(customerRepository.findByEmail(email)).thenReturn(Optional.of(mockCustomer));

        String result = profileService.updateData(dto, email);

        assertEquals("Updated Successfully", result);
        verify(customerRepository).save(mockCustomer);
        verify(customerAddressRepository).deleteByCustomer(mockCustomer);
        verify(adminRepository, never()).findByEmail(email);
    }
    @Test
    public void updateData_ShouldUpdateAdmin_WhenEmailBelongsToAdmin() {
        String email = "admin@example.com";
        UserProfileDTO dto = createMockUserProfileDTO();
        Admin mockAdmin = createMockAdmin();
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(email)).thenReturn(Optional.of(mockAdmin));

        String result = profileService.updateData(dto, email);

        assertEquals("Updated Successfully", result);
        verify(adminRepository).save(mockAdmin);
    }
    @Test
    public void updateData_ShouldReturnNotFound_WhenEmailDoesNotExist() {
        String email = "notfound@example.com";
        UserProfileDTO dto = createMockUserProfileDTO();
        when(customerRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        String result = profileService.updateData(dto, email);

        // Assert
        assertEquals("User not found with email: " + email, result);
    }
    @Test
    public void updateData_ShouldHandleException() {
        String email = "error@example.com";
        UserProfileDTO dto = createMockUserProfileDTO();
        when(customerRepository.findByEmail(email)).thenThrow(new RuntimeException());

        String result = profileService.updateData(dto, email);

        assertEquals("An error occurred\nPlease try again", result);
    }

    private UserProfileDTO createMockUserProfileDTO(){
        UserProfileDTO mockDTO = new UserProfileDTO();
        mockDTO.setFirstName("mohamed");
        mockDTO.setLastName("ahmed");
        mockDTO.setPhoneNumber("0123456789");
        List<String> addresses = Arrays.asList("123 Mock Street", "456 Mock city");
        mockDTO.setAddresses(addresses);
        mockDTO.setContactPhone("987-654-3210"); // for admin
        return mockDTO;
    }
}
