package com.olympus.mapper;

import com.olympus.dto.request.PostUpdate;
import com.olympus.entity.Post;
import com.olympus.entity.Privacy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PostUpdateMapperImplTest {
    private PostUpdateMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostUpdateMapper.class);
    }

    @Test
    void whenUpdateEntity_ThenEntityShouldBeUpdated() {
        // Arrange
        PostUpdate dto = new PostUpdate();
        dto.setContent("Updated content");
        dto.setPrivacy("private");

        Post entity = new Post();
        entity.setContent("Original content");
        entity.setPrivacy(Privacy.PUBLIC);
        entity.setUpdatedTime(LocalDateTime.MIN);

        // Act
        mapper.updateEntity(dto, entity);

        // Assert
        assertEquals("Updated content", entity.getContent());
        assertEquals("PRIVATE", entity.getPrivacy().name()); // Checking custom privacy mapping
        assertTrue(entity.getUpdatedTime().isAfter(LocalDateTime.MIN)); // Updated time should be after the original set time
    }
}