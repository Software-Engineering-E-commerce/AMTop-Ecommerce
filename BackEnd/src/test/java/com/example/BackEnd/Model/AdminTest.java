package com.example.BackEnd.Model;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerAddressRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import com.sun.source.tree.AssertTree;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import java.util.Optional;


@SpringBootTest
class AdminTest {

    @Autowired
    private AdminRepository adminRepository;
    @Mock
    private AdminRepository mockedAdminRepository;

    @Test
    public void testUserDefaultConstructor() {
        User user = new User();
        assertNotNull(user);
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());

    }

    @Test
    public void testUserParameterizedConstructor() {
        User user = new User("test@example.com", "password", false, false, "John", "Doe", "123456789");
        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertFalse(user.getIsVerified());
    }

    @Test
    public void testAdminDefaultConstructor() {
        Admin admin = new Admin();
        assertNotNull(admin);
        assertNull(admin.getAddress());
        assertNull(admin.getEmail());
        assertNull(admin.getPassword());
    }

    @Test
    public void testAdminParameterizedConstructor() {
        Admin admin = new Admin(1L, "el montazah", "03234787");
        admin.setEmail("test@example.com");
        admin.setPassword("password");
        assertNotNull(admin);
        assertEquals("el montazah", admin.getAddress());
        assertEquals("03234787", admin.getContactPhone());
        assertEquals(admin.getPassword(), "password");
    }
    @Test
    public void testMockedAdminRepoCreateRead() {
        String email = "test@example.com";
        Admin admin = new Admin(8L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");

        //simulate the saving admin by using the mocked repo
        when(mockedAdminRepository.save(any(Admin.class))).thenReturn(admin);
        Admin savedAdmin = mockedAdminRepository.save(admin);

        assertNotNull(savedAdmin);
        assertEquals(email, savedAdmin.getEmail());
        assertEquals("password", savedAdmin.getPassword());
        assertEquals(savedAdmin.getAddress(), "el montazah");
        assertNotEquals(savedAdmin.getFirstName(), null);
        assertNotEquals(savedAdmin.getLastName(), "Zian");

    }

    @Test
    @Transactional
    @DirtiesContext
    public void testAdminRepoCreateRead() {
        String email = "test@example.com";
        Admin admin = new Admin(8L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");
        admin.setIsGmail(false);
        admin.setIsVerified(false);

        //search for unsaved admin
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        assertFalse(optionalAdmin.isPresent());

        //save it
        adminRepository.save(admin);

        //search for the saved admin
        optionalAdmin = adminRepository.findByEmail(email);
        assertTrue(optionalAdmin.isPresent());

        //verify its attributes
        assertEquals("test@example.com", optionalAdmin.get().getEmail());
        assertEquals("password", optionalAdmin.get().getPassword());

        assertEquals(optionalAdmin.get().getAddress(), "el montazah");
        assertNotEquals(optionalAdmin.get().getFirstName(), null);
        assertNotEquals(optionalAdmin.get().getLastName(), "Zian");
    }

    @Test
    public void testMockedUpdate() {
        String email = "test@example.com";
        Admin admin = new Admin(8L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");

        //use mocked repo to simulate the operation of saving an admin then update its attributes
        when(mockedAdminRepository.findByEmail(email)).thenReturn(Optional.empty());
        Optional<Admin> foundAdmin = mockedAdminRepository.findByEmail(email);
        assertFalse(foundAdmin.isPresent());

        when(mockedAdminRepository.save(any(Admin.class))).thenReturn(admin);
        Admin savedAdmin = mockedAdminRepository.save(admin);

        assertNotNull(savedAdmin);
        assertEquals(email, savedAdmin.getEmail());
        assertEquals("password", savedAdmin.getPassword());
        assertEquals(savedAdmin.getAddress(), "el montazah");
        assertNotEquals(savedAdmin.getFirstName(), null);
        assertNotEquals(savedAdmin.getLastName(), "Zian");

        admin.setLastName("Zian");
        admin.setAddress("elmandra");

        Admin updatedAdmin = mockedAdminRepository.save(admin);
        assertNotNull(updatedAdmin);
        assertEquals(email, updatedAdmin.getEmail());
        assertEquals("password", updatedAdmin.getPassword());
        assertEquals(updatedAdmin.getAddress(), "elmandra");
        assertNotEquals(updatedAdmin.getFirstName(), null);
        assertEquals(updatedAdmin.getLastName(), "Zian");
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testAdminUpdate() {

        String email = "test@example.com";
        Admin admin = new Admin(1L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");
        admin.setIsGmail(false);
        admin.setIsVerified(false);

        //test the update by saving an admin then update it and check its updated attributes
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        assertFalse(optionalAdmin.isPresent());

        adminRepository.save(admin);

        optionalAdmin = adminRepository.findByEmail(email);
        assertTrue(optionalAdmin.isPresent());

        assertEquals("test@example.com", optionalAdmin.get().getEmail());
        assertEquals("password", optionalAdmin.get().getPassword());

        assertEquals(optionalAdmin.get().getAddress(), "el montazah");
        assertNotEquals(optionalAdmin.get().getFirstName(), null);
        assertNotEquals(optionalAdmin.get().getLastName(), "Zian");

        admin.setIsVerified(true);
        admin.setFirstName("omar");
        adminRepository.save(admin);

        optionalAdmin = adminRepository.findByEmail(email);
        assertTrue(optionalAdmin.isPresent());

        assertEquals(email, optionalAdmin.get().getEmail());
        assertEquals("password", optionalAdmin.get().getPassword());

        assertEquals(optionalAdmin.get().getAddress(), "el montazah");
        assertEquals(optionalAdmin.get().getFirstName(), "omar");
        assertNotEquals(optionalAdmin.get().getLastName(), "Zian");
    }

    @Test
    public void testMockedDelete() {
        String email = "test@example.com";
        Admin admin = new Admin(1L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");


        mockedAdminRepository.delete(admin);

        // Verify that the delete method was called with the expected argument
        verify(mockedAdminRepository).delete(admin);
    }

    @Test
    @Transactional
    @DirtiesContext
    public void testAdminDelete() {

        String email = "test@example.com";
        Admin admin = new Admin(1L, "el montazah", "032347875");
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setFirstName("mahmoud");
        admin.setLastName("Attia");
        admin.setIsGmail(false);
        admin.setIsVerified(false);

        //test the delete admin by adding then deleting the add again
        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        assertFalse(optionalAdmin.isPresent());

        adminRepository.save(admin);

        optionalAdmin = adminRepository.findByEmail(email);
        assertTrue(optionalAdmin.isPresent());

        assertEquals("test@example.com", optionalAdmin.get().getEmail());
        assertEquals("password", optionalAdmin.get().getPassword());

        adminRepository.delete(admin);

        optionalAdmin = adminRepository.findByEmail(email);
        assertFalse(optionalAdmin.isPresent());

        adminRepository.save(admin);
        optionalAdmin = adminRepository.findByEmail(email);
        assertTrue(optionalAdmin.isPresent());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Set up
        Admin admin1 = new Admin();
        admin1.setId(1L);
        admin1.setAddress("Admin Address");
        admin1.setContactPhone("123456789");

        Admin admin2 = new Admin();
        admin2.setId(1L);
        admin2.setAddress("Admin Address");
        admin2.setContactPhone("123456789");

        Admin admin3 = new Admin();
        admin3.setId(2L);
        admin3.setAddress("Different Address");
        admin3.setContactPhone("987654321");

        // Mocking repository behavior
        when(mockedAdminRepository.findById(1L)).thenReturn(Optional.of(admin1));
        when(mockedAdminRepository.findById(2L)).thenReturn(Optional.of(admin3));

        // Verify
        assertEquals(admin1, admin2);
        assertNotEquals(admin1, admin3);
        assertEquals(admin1.hashCode(), admin2.hashCode());
        assertNotEquals(admin1.hashCode(), admin3.hashCode());
    }

}