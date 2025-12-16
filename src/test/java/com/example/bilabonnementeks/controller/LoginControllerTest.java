package com.example.bilabonnementeks.controller;

import com.example.bilabonnementeks.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    private JdbcTemplate jdbcTemplate;
    private LoginService loginService; // den manglende afhængighed
    private HttpSession session;
    private RedirectAttributes redirectAttributes;
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        loginService = mock(LoginService.class); // mock LoginService
        session = mock(HttpSession.class);
        redirectAttributes = mock(RedirectAttributes.class);

        loginController = new LoginController(jdbcTemplate, loginService);
    }

    @Test
    void handleLogin() {

        // Arrange
        String username = "testuser";
        String password = "password123";
        String role = "dataregistrator";

        when(jdbcTemplate.query(
                anyString(),
                any(Object[].class),
                any(ResultSetExtractor.class)
        )).thenReturn(Optional.of(role));

        // Act
        String result = loginController.handleLogin(
                username,
                password,
                session,
                redirectAttributes
        );

        // Assert
        verify(session).setAttribute("username", username);
        verify(session).setAttribute("userRole", role);
        assertEquals("redirect:/dr/menu", result);
    }
}