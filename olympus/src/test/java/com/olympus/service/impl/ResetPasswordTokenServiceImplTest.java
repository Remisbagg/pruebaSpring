package com.olympus.service.impl;

import com.google.common.hash.Hashing;
import com.olympus.dto.request.AccountPasswordResetToken;
import com.olympus.entity.ResetPwdToken;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.repository.IResetPwdTokenRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResetPasswordTokenServiceImplTest {
    @InjectMocks
    private ResetPasswordTokenServiceImpl resetPasswordTokenService;
    @Mock
    private IResetPwdTokenRepository resetPwdTokenRepository;

    @Test
    void testExistByTokenAndEmail_Exist() {
        // Arrange
        String token = "some-token";
        String email = "user@example.com";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        resetPwdToken.setId(1L);
        resetPwdToken.setUser(new User(1L));
        resetPwdToken.setToken(hashedToken);
        resetPwdToken.setCreatedTime(LocalDateTime.now().minusMinutes(3)); // Token created 1 minute ago

        when(resetPwdTokenRepository.findByTokenAndUser_Email(anyString(), eq(email)))
                .thenReturn(Optional.of(resetPwdToken));

        // Act
        boolean exists = resetPasswordTokenService.existByTokenAndEmail(token, email);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistByTokenAndEmail_NotExist() {
        // Arrange
        String token = "some-token";
        String email = "user@example.com";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        resetPwdToken.setId(1L);
        resetPwdToken.setUser(new User(1L));
        resetPwdToken.setToken(hashedToken);
        resetPwdToken.setCreatedTime(LocalDateTime.now().minusMinutes(3)); // Token created 1 minute ago

        when(resetPwdTokenRepository.findByTokenAndUser_Email(anyString(), eq(email)))
                .thenReturn(Optional.empty());

        // Act
        boolean exists = resetPasswordTokenService.existByTokenAndEmail(token, email);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testCreateToken_NonExistToken() {
        // Arrange
        User user = new User(); // Assuming User is a valid user object
        when(resetPwdTokenRepository.findByUser(user)).thenReturn(Optional.empty());

        // Act
        resetPasswordTokenService.createToken(user, "some-token");

        // Assert
        verify(resetPwdTokenRepository, times(1)).save(any(ResetPwdToken.class));
    }

    @Test
    void testCreateToken_ExistingToken() {
        // Arrange
        User user = new User(); // Assuming User is a valid user object
        ResetPwdToken existingToken = new ResetPwdToken(user, "old-token");
        when(resetPwdTokenRepository.findByUser(user)).thenReturn(Optional.of(existingToken));

        // Act
        resetPasswordTokenService.createToken(user, "new-token");

        // Assert
        verify(resetPwdTokenRepository, times(1)).save(existingToken);
        assertNotEquals("old-token", existingToken.getToken()); // Ensure the token is updated
    }

    @Test
    void testRest_AccountPasswordResetTokenExist() {
        // Arrange
        AccountPasswordResetToken accountToken = new AccountPasswordResetToken();
        accountToken.setEmail("user@email.com");
        accountToken.setToken("hashedToken");
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        when(resetPwdTokenRepository.findByTokenAndUser_Email(anyString(), eq(accountToken.getEmail())))
                .thenReturn(Optional.of(resetPwdToken));

        // Act
        resetPasswordTokenService.reset(accountToken);

        // Assert
        verify(resetPwdTokenRepository, times(1)).save(any(ResetPwdToken.class));
    }

    @Test
    void testRest_AccountPasswordResetTokenNotFound() {
        // Arrange
        AccountPasswordResetToken accountToken = new AccountPasswordResetToken();
        accountToken.setEmail("user@email.com");
        accountToken.setToken("hashedToken");
        when(resetPwdTokenRepository.findByTokenAndUser_Email(anyString(), eq(accountToken.getEmail())))
                .thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> resetPasswordTokenService.reset(accountToken));
    }

    @Test
    void testExistByToken_Exist() {
        // Arrange
        String token = "some-token";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        resetPwdToken.setId(1L);
        resetPwdToken.setUser(new User(1L));
        resetPwdToken.setToken(hashedToken);
        resetPwdToken.setCreatedTime(LocalDateTime.now().minusMinutes(3)); // Token created 1 minute ago

        when(resetPwdTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(resetPwdToken));

        // Act
        boolean exists = resetPasswordTokenService.existByToken(token);

        // Assert
        assertTrue(exists);
    }

    @Test
    void testExistByToken_NotExist() {
        // Arrange
        String token = "some-token";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        resetPwdToken.setId(1L);
        resetPwdToken.setUser(new User(1L));
        resetPwdToken.setToken(hashedToken);
        resetPwdToken.setCreatedTime(LocalDateTime.now().minusMinutes(3)); // Token created 1 minute ago

        when(resetPwdTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.empty());

        // Act
        boolean exists = resetPasswordTokenService.existByToken(token);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testReset_TokenExist() {
        // Arrange
        AccountPasswordResetToken accountToken = new AccountPasswordResetToken();
        String token = "12345";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        accountToken.setEmail("user@email.com");
        accountToken.setToken(hashedToken);
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        when(resetPwdTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.of(resetPwdToken));

        // Act
        resetPasswordTokenService.reset(token);

        // Assert
        verify(resetPwdTokenRepository, times(1)).save(any(ResetPwdToken.class));
    }

    @Test
    void testReset_TokenNotExist() {
        // Arrange
        String token = "12345";
        when(resetPwdTokenRepository.findByToken(anyString()))
                .thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(UserNotFoundException.class, () -> resetPasswordTokenService.reset(token));
    }

    @Test
    void testFindEmailByTokenSuccess() {
        // Arrange
        String token = "validToken";
        String hashedToken = Hashing.sha256()
                .hashString(token, StandardCharsets.UTF_8)
                .toString();
        String expectedEmail = "test@example.com";
        ResetPwdToken resetPwdToken = mock(ResetPwdToken.class);
        User mockUser = mock(User.class);

        when(resetPwdToken.getUser()).thenReturn(mockUser);
        when(mockUser.getEmail()).thenReturn(expectedEmail);
        when(resetPwdTokenRepository.findByToken(hashedToken)).thenReturn(Optional.of(resetPwdToken));

        // Act
        String result = resetPasswordTokenService.findEmailByToken(token);

        // Assert
        assertEquals(expectedEmail, result);
    }

    @Test
    void testFindEmailByTokenThrowsException() {
        // Arrange
        String invalidToken = "invalidToken";
        String hashedToken = Hashing.sha256()
                .hashString(invalidToken, StandardCharsets.UTF_8)
                .toString();

        when(resetPwdTokenRepository.findByToken(hashedToken)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> resetPasswordTokenService.findEmailByToken(invalidToken));
    }

}