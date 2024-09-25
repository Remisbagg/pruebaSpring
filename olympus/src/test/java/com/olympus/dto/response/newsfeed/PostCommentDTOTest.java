package com.olympus.dto.response.newsfeed;

import com.olympus.dto.response.PostCommentUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostCommentDTOTest {
    @Test
    void testPostCommentDTO() {
        // Arrange
        Long commentId = 1L;
        PostCommentUser user = new PostCommentUser();
        String content = "Test content";
        LocalDateTime createdTime = LocalDateTime.now();
        LocalDateTime updatedTime = LocalDateTime.now();

        // Act
        PostCommentDTO dto = new PostCommentDTO();
        dto.setCommentId(commentId);
        dto.setUser(user);
        dto.setContent(content);
        dto.setCreatedTime(createdTime);
        dto.setUpdatedTime(updatedTime);

        // Assert
        assertEquals(commentId, dto.getCommentId());
        assertEquals(user, dto.getUser());
        assertEquals(content, dto.getContent());
        assertEquals(createdTime, dto.getCreatedTime());
        assertEquals(updatedTime, dto.getUpdatedTime());
    }
}