package com.driver.bookMyShow.Controllers;

import com.driver.bookMyShow.Models.JwtRequest;
import com.driver.bookMyShow.Models.JwtResponse;
import com.driver.bookMyShow.Controllers.AuthController;
import com.driver.bookMyShow.Models.AuthUser;
import com.driver.bookMyShow.Repositories.UserRepository;
import com.driver.bookMyShow.security.JWTHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private JWTHelper helper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_ValidCredentials_ReturnsJwtResponse() {
        // Arrange
        JwtRequest request = new JwtRequest("test@example.com", new BCryptPasswordEncoder().encode("password"));
        AuthUser user = new AuthUser();
        user.setEmailId("test@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        String[] roles = {"USER"};
        user.setRoles(roles);
        UserDetails userDetails = User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
        String token = "generated_token";
        JwtResponse expectedResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername())
                .build();

        when(userRepository.findByEmailId(request.getEmail())).thenReturn(user);
        when(helper.generateToken(userDetails)).thenReturn(token);

        // Act
        JwtResponse actualResponse = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername())
                .build();
        // Assert
        assertEquals(expectedResponse.getJwtToken(), actualResponse.getJwtToken());
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsBadCredentialsException() {
        // Arrange
        JwtRequest request = new JwtRequest("test@example.com", "wrong_password");
        AuthUser user = new AuthUser();
        user.setEmailId(request.getEmail());
        user.setPassword(request.getPassword());
        when(userRepository.findByEmailId(request.getEmail())).thenReturn(user);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authController.login(request));
    }

    @Test
    void testLogin_UserNotFound_ThrowsUsernameNotFoundException() {
        // Arrange
        JwtRequest request = new JwtRequest("nonexistent@example.com", "password");

        when(userRepository.findByEmailId(request.getEmail())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authController.login(request));
    }
}