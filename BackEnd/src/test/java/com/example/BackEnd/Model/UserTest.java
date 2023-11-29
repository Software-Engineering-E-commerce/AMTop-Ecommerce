package com.example.BackEnd.Model;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Mock
    private User user;

    @Test
    public void testGettersAndSetters() {
        // Set up
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setIsGmail(true);
        user.setIsVerified(false);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhoneNumber("123456789");

        // Verify
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertTrue(user.getIsGmail());
        assertFalse(user.getIsVerified());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("123456789", user.getPhoneNumber());
    }

    @Test
    public void testUserDetailsMethods() {
        // Set up
        user = new User();
        user.setEmail("test@example.com");

        // Verify
        assertEquals("test@example.com", user.getUsername());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    public void testGetAuthorities() {
        // Set up
        user = new User();

        // Verify
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertNull(authorities); // Assuming the user has no authorities
    }

    @Test
    public void testEqualsAndHashCode() {
        // Set up
        User user1 = new User();
        user1.setEmail("test@example.com");
        user1.setPassword("password");

        User user2 = new User();
        user2.setEmail("test@example.com");
        user2.setPassword("password");

        User user3 = new User();
        user3.setEmail("different@example.com");
        user3.setPassword("password");

        // Verify
        assertEquals(user1, user2);
        assertNotEquals(user1, user3);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertNotEquals(user1.hashCode(), user3.hashCode());
    }

    @Test
    public void testEqualsWithNull() {
        user = new User();
        assertNotEquals(null, user);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        user = new User();
        assertNotEquals("Not a User object", user);
    }

    @Test
    public void testToString() {
        user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        assertEquals("User(email=test@example.com, password=null, isGmail=null, isVerified=null, " +
                "firstName=John, lastName=Doe, phoneNumber=null)", user.toString());
    }

    @Test
    public void testBuilder() {
        // Set up
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .isGmail(true)
                .isVerified(true)
                .firstName("John")
                .lastName("Doe")
                .phoneNumber("123456789")
                .build();

        // Verify
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertTrue(user.getIsGmail());
        assertTrue(user.getIsVerified());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("123456789", user.getPhoneNumber());
    }

    @Test
    public void testBuilderDefaultValues() {
        // Set up
        User user = User.builder()
                .email("test@example.com")
                .build();

        // Verify
        assertEquals("test@example.com", user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getIsGmail());
        assertNull(user.getIsVerified());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPhoneNumber());
    }

    @Test
    public void testBuilderChaining() {
        // Set up
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .isGmail(true)
                .build();

        // Verify
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
        assertTrue(user.getIsGmail());
        assertNull(user.getIsVerified());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getPhoneNumber());
    }
}