package com.example.BackEnd.Controllers;
import com.example.BackEnd.DTO.HomeInfo;
import com.example.BackEnd.Services.HomeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class HomePageControllerTest {

    @Mock
    private HomeService homeService;

    @InjectMocks
    private HomePageController homePageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startupCustomerTest() {
        // Mock data
        String mockToken = "mockToken";
        HomeInfo mockHomeInfo = new HomeInfo("John", "Doe", false, null);

        // Mock the service behavior
        when(homeService.getHomeInfo(mockToken)).thenReturn(mockHomeInfo);

        // Perform the test
        ResponseEntity<HomeInfo> responseEntity = homePageController.startup("Bearer " + mockToken);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockHomeInfo, responseEntity.getBody());
    }

    @Test
    void startupAdminTest() {
        // Mock data
        String mockToken = "mockToken";
        HomeInfo mockHomeInfo = new HomeInfo("Admin", "User", true, null);

        // Mock the service behavior
        when(homeService.getHomeInfo(mockToken)).thenReturn(mockHomeInfo);

        // Perform the test
        ResponseEntity<HomeInfo> responseEntity = homePageController.startup("Bearer " + mockToken);

        // Assert the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockHomeInfo, responseEntity.getBody());
    }

    @Test
    void startupIllegalArgumentExceptionTest() {
        // Mock data
        String mockToken = "invalidToken";

        // Mock the service behavior to throw IllegalArgumentException
        when(homeService.getHomeInfo(mockToken)).thenThrow(new IllegalArgumentException("Invalid token"));

        // Perform the test
        ResponseEntity<HomeInfo> responseEntity = homePageController.startup("Bearer " + mockToken);

        // Assert the response
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    void extractTokenIllegalArgumentExceptionTest() {
        // Mock data
        String invalidAuthorizationHeader = "InvalidToken";

        // Perform the test and assert that IllegalArgumentException is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            homePageController.extractToken(invalidAuthorizationHeader);
        });

        // Assert the exception message if needed
        assertEquals("Authorization header doesn't exist or is in the wrong format", exception.getMessage());
    }

}
