package com.olympus.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostLikeTest {
    @Test
    void testPostLikeEntity() {
        // Arrange
        Long id = 1L;
        Post post = new Post(1L);
        User user = new User(1L);
        LocalDateTime createdTime = LocalDateTime.now();

        // Act
        PostLike postLike = new PostLike();
        postLike.setId(id);
        postLike.setPost(post);
        postLike.setUser(user);
        postLike.setCreatedTime(createdTime);

        // Assert
        assertEquals(id, postLike.getId());
        assertEquals(post, postLike.getPost());
        assertEquals(user, postLike.getUser());
        assertEquals(createdTime, postLike.getCreatedTime());
    }
}