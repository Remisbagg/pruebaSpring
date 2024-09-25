package com.olympus.mapper;

import com.olympus.dto.response.newsfeed.PostCommentDTO;
import com.olympus.entity.PostComment;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class PostCommentMapperImplTest {
    private PostCommentMapper mapper;

    @BeforeEach
    public void setUp() {
        // Initialize the mapper. MapStruct provides a way to instantiate the mapper without spring context
        mapper = Mappers.getMapper(PostCommentMapper.class);
    }

    @Test
    public void testToDTO_NullEntity() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    public void testToDTO_EntityToDTO() {
        // Given
        User user = new User(1L);
        PostComment entity = new PostComment();
        entity.setId(1L);
        entity.setUser(user);

        // When
        PostCommentDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getCommentId());
        assertEquals(entity.getContent(), dto.getContent());
        assertEquals(entity.getUser().getId(), dto.getUser().getUserId());
    }

    @Test
    public void testToDTO_UserIsNull() {
        // Given
        PostComment entity = new PostComment();
        entity.setId(1L);

        // When
        PostCommentDTO dto = mapper.toDTO(entity);

        // Then
        assertNotNull(dto);
        assertNull(dto.getUser());
    }

    @Test
    public void testToListDTO_EmptyList() {
        // Given
        List<PostComment> emptyList = Collections.emptyList();

        // When
        List<PostCommentDTO> dtoList = mapper.toListDTO(emptyList);

        // Then
        assertNotNull(dtoList);
        assertEquals(0, dtoList.size());
    }
}