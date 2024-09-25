package com.olympus.config.jwt;

import com.olympus.config.security.AuthDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {
    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthDetailsServiceImpl userDetailsService;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        SecurityContextHolder.clearContext();  // Clearing security context
    }

    @Test
    void testDoFilterInternal_ValidJwt() throws Exception {
        // Arrange
        String jwt = "valid.jwt.token";
        String email = "user@example.com";

        when(jwtProvider.isTokenValidated(jwt)).thenReturn(true);
        when(jwtProvider.getUserEmailFromJwt(jwt)).thenReturn(email);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // Add a JWT to the request header
        request.addHeader("Authorization", "Bearer " + jwt);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService).loadUserByUsername(email);
        verify(filterChain).doFilter(request, response);

        // Verify Security Context is updated
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(userDetails, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }

    @Test
    void testDoFilterInternal_InvalidJwt() throws Exception {
        // Arrange
        String jwt = "invalid.jwt.token";

        when(jwtProvider.isTokenValidated(jwt)).thenReturn(false);

        // Add a JWT to the request header
        request.addHeader("Authorization", "Bearer " + jwt);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);

        // Verify Security Context is not updated
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void testDoFilterInternal_ThrowException() throws Exception {
        // Arrange
        String jwt = "invalid.jwt.token";

        when(jwtProvider.isTokenValidated(jwt)).thenThrow(NullPointerException.class);

        // Add a JWT to the request header
        request.addHeader("Authorization", "Bearer " + jwt);

        // Act
        jwtFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(userDetailsService, never()).loadUserByUsername(anyString());
        verify(filterChain).doFilter(request, response);

        // Verify Security Context is not updated
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}