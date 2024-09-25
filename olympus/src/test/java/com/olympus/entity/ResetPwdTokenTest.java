package com.olympus.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ResetPwdTokenTest {
    @Test
    void testResetPwdTokenEntity() {
        // Arrange
        Long id = 1L;
        User user = new User(1L);
        String token = "randomToken";
        LocalDateTime createdTime = LocalDateTime.now();

        // Act
        ResetPwdToken resetPwdToken = new ResetPwdToken();
        resetPwdToken.setId(id);
        resetPwdToken.setUser(user);
        resetPwdToken.setToken(token);
        resetPwdToken.setCreatedTime(createdTime);

        // Assert
        assertEquals(id, resetPwdToken.getId());
        assertEquals(user, resetPwdToken.getUser());
        assertEquals(token, resetPwdToken.getToken());
        assertEquals(createdTime, resetPwdToken.getCreatedTime());
    }

    @Test
    void testResetPwdTokenConstructor() {
        // Arrange
        User user = new User(1L);
        String token = "randomToken123";

        // Act
        ResetPwdToken resetPwdToken = new ResetPwdToken(user, token);

        // Assert
        assertNotNull(resetPwdToken.getCreatedTime()); // Verify createdTime is set
        assertEquals(user, resetPwdToken.getUser());
        assertEquals(token, resetPwdToken.getToken());
    }
}