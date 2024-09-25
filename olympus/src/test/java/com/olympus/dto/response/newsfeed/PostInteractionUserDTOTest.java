package com.olympus.dto.response.newsfeed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PostInteractionUserDTOTest {
    @Test
    void testPostInteractionUserDTO() {
        // Create an instance of the DTO
        PostInteractionUserDTO dto = new PostInteractionUserDTO();

        // Set values
        dto.setUserId(100L);
        dto.setEmail("user@example.com");
        dto.setFirstName("Hung");
        dto.setLastName("Vo");
        dto.setAvatarUrl("avatar.jpg");

        // Assert the values using AssertJ
        assertThat(dto.getUserId()).isEqualTo(100L);
        assertThat(dto.getEmail()).isEqualTo("user@example.com");
        assertThat(dto.getFirstName()).isEqualTo("Hung");
        assertThat(dto.getLastName()).isEqualTo("Vo");
        assertThat(dto.getAvatarUrl()).isEqualTo("avatar.jpg");
    }
}