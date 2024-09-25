package com.olympus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olympus.config.security.AuthDetailsImpl;
import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.AuthRequest;
import com.olympus.service.IAuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
class AuthenticationControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private IAuthenticationService authenticationService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "spring")
    void testAuthenticateUser_Success() throws Exception {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setEmail("user@email.com");
        authRequest.setCode("123456");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        AuthDetailsImpl authDetails = mock(AuthDetailsImpl.class);
        when(authentication.getPrincipal()).thenReturn(authDetails);

        String expectedToken = "mockedToken";
        when(jwtProvider.generateToken(any(AuthDetailsImpl.class))).thenReturn(expectedToken);

        // When & Then
        mockMvc.perform(post("/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authRequest)))
                .andExpect(status().isOk()); // Validate the status

        // Optionally verify interactions
        verify(authenticationManager, times(1)).authenticate(any());
        verify(jwtProvider, times(1)).generateToken(authDetails);
        verify(authenticationService, times(1)).reset(anyString());
    }

    @Test
    @WithMockUser(value = "spring")
    void testAuthenticate_AuthenticateSuccess() throws Exception {
        mockMvc.perform(get("/v1/auth/test")) // Perform the GET request
                .andExpect(status().isOk()); // Assert that the status is 200 OK
    }
}