package com.olympus.mapper;

import com.olympus.dto.request.PostCommentCreate;
import com.olympus.entity.PostComment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostCommentCreateMapperImplTest {
    private PostCommentCreateMapper mapper;

    @BeforeEach
    void setup() {
        mapper = Mappers.getMapper(PostCommentCreateMapper.class);
    }

    @Test
    void shouldMapDtoToEntity() {
        // Arrange
        PostCommentCreate dto = new PostCommentCreate();
        dto.setContent("Test content");

        // Act
        PostComment entity = mapper.dtoToEntity(dto);

        // Assert
        assertNotNull(entity);
        assertEquals(dto.getContent(), entity.getContent());
        assertNotNull(entity.getCreatedTime());
        assertNotNull(entity.getUpdatedTime());
        assertFalse(entity.isDeleteStatus());
    }

    @Test
    void whenNull_ThenReturnNull() {
        assertNull(mapper.dtoToEntity(null));
    }
}