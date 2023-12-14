package com.example.BackEnd.Controllers;

import com.example.BackEnd.Config.JwtService;
import com.example.BackEnd.Controllers.ProfileController;
import com.example.BackEnd.DTO.UserProfileDTO;
import com.example.BackEnd.Services.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;

    @Mock
    private ProfileService profileService;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void retrieve_ShouldReturnProfileData_WhenValidToken() {
        String authorizationHeader = "Bearer validtoken";
        String email = "user@example.com";
        UserProfileDTO mockDto = createMockUserProfileDTO();

        when(jwtService.extractUsername("validtoken")).thenReturn(email);
        when(profileService.retrieveData(email)).thenReturn(mockDto);

        ResponseEntity<UserProfileDTO> response = profileController.retrieve(authorizationHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDto, response.getBody());

        verify(jwtService).extractUsername("validtoken");
        verify(profileService).retrieveData(email);
    }
    @Test
    public void retrieve_ShouldReturnForbidden_WhenAuthorizationHeaderIsInvalid() {
        String authorizationHeader = "Invalid header";

        ResponseEntity<UserProfileDTO> response = profileController.retrieve(authorizationHeader);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNull(response.getBody());
    }
    @Test
    public void updateData_ShouldReturnSuccessMessage_WhenValidToken() {
        String authorizationHeader = "Bearer validtoken";
        String email = "user@example.com";
        UserProfileDTO userProfileDTO = createMockUserProfileDTO();

        when(jwtService.extractUsername("validtoken")).thenReturn(email);
        when(profileService.updateData(userProfileDTO, email)).thenReturn("Updated Successfully");

        ResponseEntity<String> response = profileController.updateData(authorizationHeader, userProfileDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Successfully", response.getBody());

        verify(jwtService).extractUsername("validtoken");
        verify(profileService).updateData(userProfileDTO, email);
    }
    @Test
    public void updateData_ShouldReturnForbidden_WhenAuthorizationHeaderIsInvalid() {
        String authorizationHeader = "Invalid header";
        UserProfileDTO userProfileDTO = createMockUserProfileDTO();

        ResponseEntity<String> response = profileController.updateData(authorizationHeader, userProfileDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Access Denied", response.getBody());
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
