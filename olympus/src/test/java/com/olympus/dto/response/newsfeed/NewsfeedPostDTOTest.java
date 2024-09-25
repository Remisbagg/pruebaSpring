package com.olympus.dto.response.newsfeed;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NewsfeedPostDTOTest {
    @Test
    void testNewsfeedPostDTO() {
        // Create an instance of the DTO
        NewsfeedPostDTO dto = new NewsfeedPostDTO();

        // Set values
        dto.setPostId(1L);
        dto.setUserId(2L);
        dto.setUserFirstname("Hung");
        dto.setUserLastname("Vo");
        dto.setUserEmail("email@example.com");
        dto.setUserAvatar("avatar.jpg");
        dto.setContent("This is a post");
        LocalDateTime now = LocalDateTime.now();
        dto.setCreatedTime(now);
        dto.setUpdatedTime(now);
        dto.setImages(Collections.emptyList());
        dto.setLikes(Collections.emptyList());
        dto.setComments(Collections.emptyList());

        // Assert the values using AssertJ
        assertThat(dto.getPostId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo(2L);
        assertThat(dto.getUserFirstname()).isEqualTo("Hung");
        assertThat(dto.getUserLastname()).isEqualTo("Vo");
        assertThat(dto.getUserEmail()).isEqualTo("email@example.com");
        assertThat(dto.getUserAvatar()).isEqualTo("avatar.jpg");
        assertThat(dto.getContent()).isEqualTo("This is a post");
        assertThat(dto.getCreatedTime()).isEqualTo(now);
        assertThat(dto.getUpdatedTime()).isEqualTo(now);
        assertThat(dto.getImages()).isEmpty();
        assertThat(dto.getLikes()).isEmpty();
        assertThat(dto.getComments()).isEmpty();
    }
}