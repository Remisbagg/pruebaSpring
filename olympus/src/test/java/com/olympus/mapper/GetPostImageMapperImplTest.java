package com.olympus.mapper;

import com.olympus.dto.response.GetPostImage;
import com.olympus.entity.Post;
import com.olympus.entity.PostImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class GetPostImageMapperImplTest {
    private GetPostImageMapper mapper;

    @BeforeEach
    void setUp() {
        // Get an instance of the mapper
        mapper = Mappers.getMapper(GetPostImageMapper.class);
    }

    @Test
    void whenEntityNull_thenNull() {
        assertNull(mapper.entityToDto(null));
    }

    @Test
    void whenEntityToDto_thenReturnDto() {
        // Arrange: Create a test PostImage entity
        PostImage postImage = new PostImage();
        postImage.setId(1L);
        postImage.setUrl("testUrl");
        Post post = new Post();  // Assuming you have a Post class that PostImage references
        post.setId(2L);
        postImage.setPost(post);

        // Act: Map the entity to a DTO
        GetPostImage getPostImage = mapper.entityToDto(postImage);

        // Assert: Validate the mapping
        assertEquals(postImage.getId().toString(), getPostImage.getImageId());
        assertEquals(postImage.getUrl(), getPostImage.getUrl());
        assertEquals(postImage.getPost().getId().toString(), getPostImage.getPostId());
    }
}