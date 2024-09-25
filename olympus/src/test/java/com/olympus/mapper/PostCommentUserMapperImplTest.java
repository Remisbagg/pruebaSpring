package com.olympus.mapper;

import com.olympus.dto.response.PostCommentUser;
import com.olympus.entity.PostComment;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostCommentUserMapperImplTest {
    private PostCommentUserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostCommentUserMapper.class);
    }

    @Test
    void whenEntityToDto_thenReturnDto() {
        // Arrange: Create a test PostComment entity with User
        User user = new User();
        user.setId(123L);
        user.setEmail("user@test.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setAvatar("avatarUrl");

        PostComment postComment = new PostComment();
        postComment.setUser(user);

        // Act: Map the entity to a DTO
        PostCommentUser dto = mapper.toDTO(postComment);

        // Assert: Validate the mapping
        assertEquals(user.getId(), dto.getUserId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getAvatar(), dto.getAvatarUrl());
    }
}