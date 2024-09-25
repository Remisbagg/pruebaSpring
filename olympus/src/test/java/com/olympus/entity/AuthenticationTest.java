package com.olympus.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AuthenticationTest {
    @Test
    void testAuthenticationEntity() {
        // Arrange
        Long id = 1L;
        User user = new User(1L);
        String code = "123456";
        LocalDateTime createdTime = LocalDateTime.now();

        // Act
        Authentication authentication = new Authentication();
        authentication.setId(id);
        authentication.setUser(user);
        authentication.setCode(code);
        authentication.setCreatedTime(createdTime);

        // Assert
        assertEquals(id, authentication.getId());
        assertEquals(user, authentication.getUser());
        assertEquals(code, authentication.getCode());
        assertEquals(createdTime, authentication.getCreatedTime());
    }

    @Test
    void testAuthenticationEntityConstructor() {
        // Arrange
        User user = new User(1L);
        String code = "123456";

        // Act
        Authentication authentication = new Authentication(user, code);

        // Assert
        assertNotNull(authentication.getCreatedTime()); // Verify createdTime is set
        assertEquals(user, authentication.getUser());
        assertEquals(code, authentication.getCode());
    }
}