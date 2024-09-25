package com.olympus.dto.response;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class OtherUserFriendshipTest {
    @Test
    void testDefaultConstructor() {
        // Act
        OtherUserFriendship friendship = new OtherUserFriendship();

        // Assert
        assertFalse(friendship.isStatus()); // default status should be false as set in constructor
    }

    @Test
    void testFieldSettersAndGetters() {
        // Arrange
        boolean status = true;
        String role = "Friend";
        Long id = 1L;

        // Act
        OtherUserFriendship friendship = new OtherUserFriendship();
        friendship.setStatus(status);
        friendship.setFriendshipId(id);

        // Assert
        assertEquals(status, friendship.isStatus());
        assertEquals(id, friendship.getFriendshipId());
    }
}