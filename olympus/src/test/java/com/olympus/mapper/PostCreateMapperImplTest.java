package com.olympus.mapper;

import com.olympus.dto.request.PostCreate;
import com.olympus.entity.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class PostCreateMapperImplTest {
    private PostCreateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostCreateMapper.class);
    }

    @Test
    void whenToPost_thenReturnPost() {
        // Arrange
        PostCreate postCreate = new PostCreate();
        postCreate.setContent("Some content");
        postCreate.setPrivacy("private");

        // Act
        Post post = mapper.toPost(postCreate);

        // Assert
        assertEquals(postCreate.getContent(), post.getContent());
        assertEquals("PRIVATE", post.getPrivacy().name()); // Checking custom privacy mapping
        assertNotNull(post.getCreatedTime()); // Asserting createdTime and updatedTime are set
        assertNotNull(post.getUpdatedTime());
        assertFalse(post.isDeleteStatus()); // Checking constant mapping
    }
}