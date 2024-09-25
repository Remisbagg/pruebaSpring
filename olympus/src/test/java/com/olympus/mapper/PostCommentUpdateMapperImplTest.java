package com.olympus.mapper;

import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.entity.PostComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostCommentUpdateMapperImplTest {
    private PostCommentUpdateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostCommentUpdateMapper.class);
    }

    @Test
    void whenUpdateEntityFromDTO_thenEntityShouldBeUpdated() {
        // Arrange
        PostComment entity = new PostComment(); // Initialize with some default values
        entity.setCreatedTime(LocalDateTime.now().minusDays(1)); // Set some time in the past
        entity.setUpdatedTime(LocalDateTime.now().minusDays(1));
        entity.setContent("Old Content");
        entity.setDeleteStatus(true);

        PostCommentUpdate dto = new PostCommentUpdate();
        dto.setContent("Updated Content");

        // Act
        mapper.updateEntityFromDTO(dto, entity);

        // Assert
        assertEquals("Updated Content", entity.getContent());
        assertNotNull(entity.getUpdatedTime());
        assertFalse(entity.isDeleteStatus());
        assertEquals(entity.getCreatedTime(), entity.getCreatedTime());
    }
}