package com.olympus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.*;
import com.olympus.service.IMailService;
import com.olympus.service.IResetPwdTokenService;
import com.olympus.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@Import(SecurityConfig.class)
class AccountControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private IUserService userService;

    @MockBean
    private IMailService mailService;

    @MockBean
    private IResetPwdTokenService resetPwdTokenService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @WithMockUser(value = "spring")
    @Test
    void testRegister_Success() throws Exception {
        //Arrange
        AccountRegister validAccount = new AccountRegister();
        validAccount.setEmail("user@email.com");
        validAccount.setPassword("password");
        when(userService.register(any(AccountRegister.class))).thenReturn(1L);

        //Act & Assert
        mockMvc.perform(post("/v1/account/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(validAccount)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "spring")
    void testLogin_Success() throws Exception {
        //Arrange
        AccountLogin validLogin = new AccountLogin();
        validLogin.setEmail("user@email.com");
        validLogin.setPassword("123456");
        when(userService.existsUserByEmailAndPassword(validLogin)).thenReturn(true);
        doNothing().when(mailService).sendLoginOTP(anyString());

        //Act & Assert
        mockMvc.perform(post("/v1/account/login").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(validLogin))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testForgotPassword_SenTokenSuccess() throws Exception {
        //Arrange
        AccountPasswordForgot accountPasswordForgot = new AccountPasswordForgot();
        accountPasswordForgot.setEmail("user@email.com");
        doNothing().when(mailService).sendPasswordResetToken(anyString());

        //Act & Assert
        mockMvc.perform(post("/v1/account/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountPasswordForgot))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testValidateResetPasswordToken() throws Exception {
        //Arrange
        AccountPasswordResetToken accountPasswordResetToken = new AccountPasswordResetToken();
        accountPasswordResetToken.setEmail("user@email.com");
        accountPasswordResetToken.setToken("token");
        when(userService.existsEmail(accountPasswordResetToken.getEmail())).thenReturn(true);
        when(resetPwdTokenService.existByTokenAndEmail(accountPasswordResetToken.getToken(), accountPasswordResetToken.getEmail())).thenReturn(true);
        doNothing().when(resetPwdTokenService).reset(anyString());

        //Act & Assert
        mockMvc.perform(post("/v1/account/validate-reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountPasswordResetToken))).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testValidateResetPwdToken() throws Exception {
        // Arrange
        String validToken = "valid-token"; // Example token
        when(resetPwdTokenService.existByToken(anyString())).thenReturn(true);

        // Assume reset method in service does not return any value (void method)
        doNothing().when(resetPwdTokenService).reset(validToken);

        // Act & Assert
        mockMvc.perform(get("/v1/account//reset-password")
                        .param("token", validToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify that service method was called
        verify(resetPwdTokenService).reset(validToken);
    }

    @Test
    @WithMockUser(value = "spring")
    void testResetPassword() throws Exception {
        //Arrange
        AccountPasswordReset accountPasswordReset = new AccountPasswordReset();
        accountPasswordReset.setEmail("user@email.com");
        accountPasswordReset.setPassword("123456");
        when(userService.existsEmail(accountPasswordReset.getEmail())).thenReturn(true);

        doNothing().when(userService).updatePassword(accountPasswordReset);

        //Act && Assert
        mockMvc.perform(post("/v1/account/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(accountPasswordReset))).andExpect(status().isOk());
    }
}