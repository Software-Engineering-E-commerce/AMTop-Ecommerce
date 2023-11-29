package com.example.BackEnd.Services;

        import com.example.BackEnd.Model.Admin;
        import com.example.BackEnd.Model.Customer;
        import com.example.BackEnd.Repositories.AdminRepository;
        import com.example.BackEnd.Repositories.CustomerRepository;
        import com.example.BackEnd.DTO.AuthenticationResponse;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.context.SpringBootTest;
        import org.springframework.test.annotation.DirtiesContext;
        import org.springframework.transaction.annotation.Transactional;

        import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
public class GmailAuthServiceTest {

    @Autowired
    private GmailAuthService gmailAuthService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        // Any setup if necessary
    }

    @Test
    @Transactional
    @DirtiesContext
    public void whenNewUser_thenShouldBeAddedToDatabase() {
        final String validJwtToken =
                "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5ld3VzZXJAZ29vZ2xlLmNvbSIsImdpdmVuX25hbWUiOiJOZXciLCJmYW1pbHlfbm" +
                        "FtZSI6IlVzZXIifQ.brcwayiUvJSsTWLDBhXQWtKCQ7SLgjM8IUnYSZIhlLc";

        String email = "newuser@google.com";
        assertFalse(customerRepository.findByEmail(email).isPresent());

        AuthenticationResponse response = gmailAuthService.googleRegister(validJwtToken);

        assertNotNull(response);
        assertTrue(customerRepository.findByEmail(email).isPresent());
    }
 
    @Test
    @Transactional
    @DirtiesContext
    public void whenExistingGmailCustomer_thenShouldReturnExistingToken() {
        final String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImV4aXN0aW5ndXNlckBnb29nbGUuY29tIiwiZ2l2ZW5fb" +
                "mFtZSI6Ik5ldyIsImZhbWlseV9uYW1lIjoiVXNlciJ9.8kljCQTbenwadUNmnCVkPznW2MJQK4AV_fo7GaJK3wo";
        String email = "existinguser@google.com";
        Customer existingCustomer = new Customer(email, "password", true, true, "Existing", "User");
        customerRepository.save(existingCustomer);

        AuthenticationResponse response = gmailAuthService.googleRegister(validJwtToken);

        assertNotNull(response);
        assertTrue(existingCustomer.getIsGmail());
        assertEquals("existinguser@google.com", customerRepository.findByEmail(email).get().getEmail());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void whenExistingNonGmailCustomer_thenShouldReturnAlreadyExists() {
        final String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5vbmdtYWlsdXNlckBnb29nbGUuY29tIiwiZ2l2ZW5fbm" +
                "FtZSI6Ik5ldyIsImZhbWlseV9uYW1lIjoiVXNlciJ9.McYaA1e1UkKN7pjhl_zoOFU94HE3EmBJY4wPkDJGMFU";
        String email = "nongmailuser@google.com";
        Customer existingCustomer = new Customer(email, "password", false, true, "NonGmail", "User");
        customerRepository.save(existingCustomer);

        AuthenticationResponse response = gmailAuthService.googleRegister(validJwtToken);

        assertNotNull(response);
        assertFalse(existingCustomer.getIsGmail());
        assertEquals("nongmailuser@google.com", customerRepository.findByEmail(email).get().getEmail());
        assertEquals("Already Exists", response.getToken());
    }

    @Test
    @Transactional
    @DirtiesContext
    public void whenExistingGmailAdmin_thenShouldReturnExistingToken() {
        final String validJwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFkbWludXNlckBnb29nbGUuY29tIiwiZ2l2ZW5fbmFtZSI" +
                "6Ik5ldyIsImZhbWlseV9uYW1lIjoiVXNlciJ9.ggY9nNn2HGCluv6JK2CPywduZdAyjuEEU8IgZnhrZHE";
        String email = "adminuser@google.com";
        Admin existingAdmin = new Admin();
        existingAdmin.setEmail(email);
        existingAdmin.setPassword("password");
        existingAdmin.setFirstName("Admin");
        existingAdmin.setLastName("User");
        existingAdmin.setIsGmail(true);
        existingAdmin.setIsVerified(true);
        adminRepository.save(existingAdmin);

        AuthenticationResponse response = gmailAuthService.googleRegister(validJwtToken);

        assertNotNull(response);
        assertEquals("adminuser@google.com", adminRepository.findByEmail(email).get().getEmail());
        assertNotNull(response.getToken());
        assertTrue(existingAdmin.getIsGmail());
        assertFalse(response.getToken().isEmpty());
    }
}

