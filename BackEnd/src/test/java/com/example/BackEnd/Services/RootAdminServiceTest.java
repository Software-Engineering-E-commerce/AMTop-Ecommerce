package com.example.BackEnd.Services;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Model.Admin;
import com.example.BackEnd.Model.Customer;
import com.example.BackEnd.Repositories.AdminRepository;
import com.example.BackEnd.Repositories.CustomerRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RootAdminServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RootAdminService rootAdminService;

    @Test
    void testAddNewAdminSuccess() throws Exception {
        // Arrange
        String token = "mockToken";
        String newAdminEmail = "newadmin@example.com";

        Admin admin = new Admin();
        admin.setId(1L);
        admin.setIsVerified(true);
        admin.setIsGmail(false);
        admin.setEmail("root@admin.com");
        when(jwtService.extractUsername(anyString())).thenReturn("root@admin.com");
        when(adminRepository.findByEmail("root@admin.com")).thenReturn(Optional.of(admin));
        when(adminRepository.findByEmail(newAdminEmail)).thenReturn(Optional.empty());
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        rootAdminService.addEmployee(token, newAdminEmail);

        // Assert
        verify(jwtService, times(1)).extractUsername(eq(token));
        verify(adminRepository, times(1)).findByEmail(eq("root@admin.com"));
        verify(customerRepository, times(1)).findByEmail(eq(newAdminEmail));
        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void reVerifyAdminSuccess() throws Exception {
        // Arrange
        String token = "mockToken";
        String newAdminEmail = "mahmoudatyaa444@gmail.com";
        String rootEmail = "root@admin.com";

        Admin rootAdmin = new Admin();
        rootAdmin.setId(1L);
        rootAdmin.setIsVerified(true);
        rootAdmin.setIsGmail(false);
        rootAdmin.setEmail(rootEmail);

        Admin newAdmin = new Admin();
        newAdmin.setId(3L);
        newAdmin.setEmail(newAdminEmail);
        newAdmin.setIsVerified(false);

        when(jwtService.extractUsername(anyString())).thenReturn(rootEmail);
        when(jwtService.generateToken(newAdmin)).thenReturn(anyString());
        when(adminRepository.findByEmail(rootEmail)).thenReturn(Optional.of(rootAdmin));
        when(adminRepository.findByEmail(newAdminEmail)).thenReturn(Optional.of(newAdmin));
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        rootAdminService.addEmployee(token, newAdminEmail);

        // Assert
        verify(jwtService, times(1)).extractUsername(eq(token));
        verify(jwtService, times(1)).generateToken(eq(newAdmin));
        verify(adminRepository, times(1)).findByEmail(eq(rootEmail));
        verify(adminRepository, times(1)).findByEmail(eq(newAdminEmail));
        verify(customerRepository, times(1)).findByEmail(eq(newAdminEmail));
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testAddEmployeeConflictCustomer() throws MessagingException {
        // Arrange
        String token = "mockToken";
        String newAdminEmail = "customer@example.com";
        String rootEmail = "root@admin.com";
        Customer customer = new Customer();
        customer.setEmail(newAdminEmail);
        customer.setId(5L);

        Admin rootAdmin = new Admin();
        rootAdmin.setId(1L);
        rootAdmin.setIsVerified(true);
        rootAdmin.setIsGmail(false);
        rootAdmin.setEmail(rootEmail);

        when(jwtService.extractUsername(anyString())).thenReturn(rootEmail);
        when(adminRepository.findByEmail(rootEmail)).thenReturn(Optional.of(rootAdmin));
        when(customerRepository.findByEmail(newAdminEmail)).thenReturn(Optional.of(customer));

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> rootAdminService.addEmployee(token, newAdminEmail));
        verify(jwtService, times(1)).extractUsername(eq(token));
        verify(customerRepository, times(1)).findByEmail(eq(newAdminEmail));
        verify(adminRepository, times(0)).save(any(Admin.class));
        verify(emailService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testAddEmployeeConflict() throws MessagingException {
        // Arrange
        String token = "mockToken";
        String newAdminEmail = "newadmin@example.com";

        when(jwtService.extractUsername(anyString())).thenReturn("root@admin.com");
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> rootAdminService.addEmployee(token, newAdminEmail));
        verify(jwtService, times(1)).extractUsername(eq(token));
        verify(adminRepository, times(1)).findByEmail(eq("root@admin.com"));
        verify(customerRepository, times(0)).findByEmail(eq(newAdminEmail));
        verify(adminRepository, times(0)).save(any(Admin.class));
        verify(emailService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testVerifyRootAdminSuccess() {
        // Arrange
        Admin rootAdmin = new Admin();
        rootAdmin.setId(1L);
        rootAdmin.setEmail("root@admin.com");

        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(rootAdmin));

        // Act and Assert
        assertDoesNotThrow(() -> rootAdminService.verifyRootAdmin("root@admin.com"));
        verify(adminRepository, times(1)).findByEmail(eq("root@admin.com"));
    }

    @Test
    void testVerifyRootAdminNotFound() {
        // Arrange
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> rootAdminService.verifyRootAdmin("root@admin.com"));
        verify(adminRepository, times(1)).findByEmail(eq("root@admin.com"));
    }

    @Test
    void testVerifyRootAdminForbidden() {
        // Arrange
        Admin nonRootAdmin = new Admin();
        nonRootAdmin.setId(2L);
        nonRootAdmin.setEmail("nonroot@admin.com");

        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(nonRootAdmin));

        // Act and Assert
        assertThrows(ResponseStatusException.class, () -> rootAdminService.verifyRootAdmin("nonroot@admin.com"));
        verify(adminRepository, times(1)).findByEmail(eq("nonroot@admin.com"));
    }

    @Test
    void testHandleFoundAdminUnverifiedAdmin() throws Exception {
        // Arrange
        Admin unverifiedAdmin = new Admin();
        unverifiedAdmin.setId(2L);
        unverifiedAdmin.setEmail("unverified@admin.com");
        unverifiedAdmin.setIsVerified(false);

        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        assertDoesNotThrow(() -> rootAdminService.handleFoundAdmin(unverifiedAdmin));

        // Assert
        verify(emailService, times(1)).sendEmail(eq("unverified@admin.com"), anyString(), anyString());
    }

    @Test
    void testHandleFoundAdminVerifiedAdmin() {
        // Arrange
        Admin verifiedAdmin = new Admin();
        verifiedAdmin.setId(3L);
        verifiedAdmin.setEmail("verified@admin.com");
        verifiedAdmin.setIsVerified(true);

        // Act and Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> rootAdminService.handleFoundAdmin(verifiedAdmin));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("This email already used by a verified admin!", exception.getReason());
    }

    @Test
    void testHandleNewAdminSuccess() throws Exception {
        // Arrange
        String newAdminEmail = "newadmin@example.com";

        // Takes the invocation of the save method
        // and returns its first argument (the Admin instance passed)
        when(adminRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        // Act
        assertDoesNotThrow(() -> rootAdminService.handleNewAdmin(newAdminEmail));

        // Assert
        verify(adminRepository, times(1)).save(any(Admin.class));
        verify(emailService, times(1)).sendEmail(eq(newAdminEmail), anyString(), anyString());
    }

    @Test
    void testNewAdminVerificationSuccess() throws Exception {
        // Arrange
        Admin admin = new Admin();
        admin.setEmail("admin@example.com");

        when(jwtService.generateToken(any(Admin.class))).thenReturn("mockToken");
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // Act
        assertDoesNotThrow(() -> rootAdminService.newAdminVerification(admin));

        // Assert
        verify(jwtService, times(1)).generateToken(eq(admin));
        verify(emailService, times(1)).sendEmail(eq("admin@example.com"), anyString(), anyString());
    }


}