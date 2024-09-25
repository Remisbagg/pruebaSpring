package com.olympus.mapper;

import com.olympus.dto.response.newsfeed.PostImageDTO;
import com.olympus.entity.PostImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PostImageMapperImplTest {
    private PostImageMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(PostImageMapper.class);
    }

    @Test
    void whenToDto_thenReturnDto() {
        // Arrange
        PostImage postImage = new PostImage();
        postImage.setId(1L);
        postImage.setUrl("image-url");

        // Act
        PostImageDTO dto = mapper.toDTO(postImage);

        // Assert
        assertEquals(postImage.getId(), dto.getId());
        assertEquals(postImage.getUrl(), dto.getUrl());
    }

    @Test
    void whenToDtoList_thenReturnDtoList() {
        // Arrange
        PostImage postImage1 = new PostImage();
        postImage1.setId(1L);
        postImage1.setUrl("image-url1");
        PostImage postImage2 = new PostImage();
        postImage2.setId(2L);
        postImage2.setUrl("image-url2");
        List<PostImage> postImages = Arrays.asList(postImage1, postImage2);

        // Act
        List<PostImageDTO> dtos = mapper.toDTOList(postImages);

        // Assert
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals(postImages.get(0).getId(), dtos.get(0).getId());
        assertEquals(postImages.get(1).getId(), dtos.get(1).getId());
        assertEquals(postImages.get(0).getUrl(), dtos.get(0).getUrl());
        assertEquals(postImages.get(1).getUrl(), dtos.get(1).getUrl());
    }
}