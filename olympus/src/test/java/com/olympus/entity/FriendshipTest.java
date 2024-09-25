package com.olympus.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class FriendshipTest {
    @Test
    void testFriendshipEntity() {
        // Arrange
        Long id = 1L;
        User user1 = new User(1L);
        User user2 = new User(2L);
        LocalDate createdTime = LocalDate.now();

        // Act
        Friendship friendship = new Friendship();
        friendship.setId(id);
        friendship.setUser1(user1);
        friendship.setUser2(user2);
        friendship.setCreatedTime(createdTime);

        // Assert
        assertEquals(id, friendship.getId());
        assertEquals(user1, friendship.getUser1());
        assertEquals(user2, friendship.getUser2());
        assertEquals(createdTime, friendship.getCreatedTime());
    }
}