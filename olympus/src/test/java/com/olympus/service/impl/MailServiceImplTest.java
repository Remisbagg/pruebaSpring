package com.olympus.service.impl;

import com.olympus.entity.User;
import com.olympus.repository.IUserRepository;
import com.olympus.service.IAuthenticationService;
import com.olympus.service.IResetPwdTokenService;
import com.olympus.service.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {
    @InjectMocks
    MailServiceImpl mailService;
    @Mock
    private JavaMailSender mailSender;
    @Mock
    private IUserService userService;
    @Mock
    private IAuthenticationService authenticationService;
    @Mock
    private IResetPwdTokenService resetPwdTokenService;
    @Mock
    private MimeMessage mimeMessage;
    @Mock
    private IUserRepository userRepository;

    @Test
    void testSendLoginOTP() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(); // mock user or real object as needed
        MimeMessage mockMessage = mock(MimeMessage.class);
        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);

        // Act
        mailService.sendLoginOTP(email);

        // Assert
        verify(mailSender).send(any(MimeMessage.class)); // Ensure mail is sent
        verify(authenticationService).createAuthentication(any(User.class), anyString()); // Ensure OTP is generated and saved
    }

    @Test
    void testSendPasswordResetToken() throws MessagingException {
        // Arrange
        String email = "test@example.com";
        User user = new User(); // mock user or real object as needed
        MimeMessage mockMessage = mock(MimeMessage.class);
        when(userService.findUserByEmail(email)).thenReturn(Optional.of(user));
        when(mailSender.createMimeMessage()).thenReturn(mockMessage);

        // Act
        mailService.sendPasswordResetToken(email);

        // Assert
        verify(mailSender).send(any(MimeMessage.class)); // Ensure mail is sent
        verify(resetPwdTokenService).createToken(any(User.class), anyString()); // Ensure reset token is generated and saved
    }

}