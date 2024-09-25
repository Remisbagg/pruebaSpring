package com.olympus.mapper;

import com.olympus.dto.response.newsfeed.PostInteractionUserDTO;
import com.olympus.entity.PostLike;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PostInteractionUserMapperImplTest {
    private PostInteractionUserMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostInteractionUserMapper.class);
    }

    @Test
    void whenToDto_thenReturnDto() {
        // Arrange
        User user = new User();
        user.setId(123L);
        user.setEmail("user@test.com");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setAvatar("avatarUrl");
        PostLike postLike = new PostLike();
        postLike.setUser(user);

        // Act
        PostInteractionUserDTO dto = mapper.toDTO(postLike);

        // Assert
        assertEquals(user.getId(), dto.getUserId());
        assertEquals(user.getEmail(), dto.getEmail());
        assertEquals(user.getFirstName(), dto.getFirstName());
        assertEquals(user.getLastName(), dto.getLastName());
        assertEquals(user.getAvatar(), dto.getAvatarUrl());
    }
}