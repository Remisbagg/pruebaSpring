package com.olympus.service.impl;

import com.olympus.entity.Authentication;
import com.olympus.entity.User;
import com.olympus.repository.IAuthenticationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private final ArgumentCaptor<Authentication> authCaptor = ArgumentCaptor.forClass(Authentication.class);
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private IAuthenticationRepository authenticationRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User mockUser;

    public void setUp() {
        mockUser = new User();
        when(passwordEncoder.encode(anyString())).thenReturn("hashedCode");
    }

    @Test
    public void testCreateAuthentication_newUser() {
        //Arrange
        setUp();
        when(authenticationRepository.findByUser(any(User.class)))
                .thenReturn(Optional.empty());

        //Act
        authenticationService.createAuthentication(mockUser, "123456");

        //Assert
        verify(authenticationRepository).save(authCaptor.capture());
        Authentication savedAuthentication = authCaptor.getValue();
        assertEquals("hashedCode", savedAuthentication.getCode());
        assertEquals(mockUser, savedAuthentication.getUser());
    }

    @Test
    public void testCreateAuthentication_ExistingUser() {
        //Arrange
        setUp();
        Authentication existingAuthentication = new Authentication(mockUser, "oldHashedCode");
        when(authenticationRepository.findByUser(any(User.class))).thenReturn(Optional.of(existingAuthentication));

        //Act
        authenticationService.createAuthentication(mockUser, "123456");

        //Assert
        verify(authenticationRepository).save(authCaptor.capture());
        Authentication updatedAuthentication = authCaptor.getValue();
        assertEquals("hashedCode", updatedAuthentication.getCode());
        assertEquals(mockUser, updatedAuthentication.getUser());
    }

    @Test
    public void testReset_Successful() {
        //Arrange
        Authentication existingAuthentication = new Authentication();
        when(authenticationRepository.findAuthenticationByUser_Email(anyString())).thenReturn(Optional.of(existingAuthentication));

        //Act
        String userEmail = "user@email.com";
        authenticationService.reset(userEmail);

        //Assert
        verify(authenticationRepository).save(authCaptor.capture());
        Authentication resetAuthentication = authCaptor.getValue();
        assertEquals("", resetAuthentication.getCode());
    }

}