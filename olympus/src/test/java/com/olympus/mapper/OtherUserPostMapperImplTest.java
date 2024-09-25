package com.olympus.mapper;

import com.olympus.dto.response.OtherUserPost;
import com.olympus.entity.Post;
import com.olympus.entity.Privacy;
import com.olympus.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class OtherUserPostMapperImplTest {
    @InjectMocks
    private OtherUserPostMapper mapper = Mappers.getMapper(OtherUserPostMapper.class);

    @Mock
    private PostImageMapper postImageMapper;
    @Mock
    private PostInteractionUserMapper postInteractionUserMapper;
    @Mock
    private PostCommentMapper postCommentMapper;

    private Post post;

    @BeforeEach
    void setUp() {
        // Initialize your post object here with test data
        post = new Post();
        post.setId(1L);
        post.setUser(new User(1L));
        post.setContent("hello world");
        post.setPrivacy(Privacy.PUBLIC);
        post.setCreatedTime(LocalDateTime.now());
        post.setUpdatedTime(LocalDateTime.now());
    }

    @Test
    void whenMapPostToDto_thenCorrect() {
        // Perform mapping
        OtherUserPost dto = mapper.toDTO(post);

        // Assertions to validate each field is mapped correctly
        assertEquals(post.getId().toString(), dto.getPostId());
        assertEquals(post.getUser().getId().toString(), dto.getUserId());
        assertEquals(post.getContent(), dto.getContent());
        //... continue for all fields, including verifying that image, likes, and comment lists are mapped
    }

    @Test
    void whenMapPostListToDtoList_thenCorrect() {
        List<Post> posts = Collections.singletonList(post);
        List<OtherUserPost> dtoList = mapper.toListDTOs(posts);

        assertNotNull(dtoList);
        assertEquals(1, dtoList.size()); // Assuming you have one post in the list
    }
}