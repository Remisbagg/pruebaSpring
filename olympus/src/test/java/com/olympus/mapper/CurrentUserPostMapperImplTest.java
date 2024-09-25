package com.olympus.mapper;

import com.olympus.dto.response.curentuserpost.CurrentUserPost;
import com.olympus.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CurrentUserPostMapperImplTest {
    private Post post;
    private List<Post> postList;
    @InjectMocks
    private CurrentUserPostMapperImpl mapper;
    @Mock
    private PostImageMapper postImageMapper;
    @Mock
    private PostCommentMapper postCommentMapper;
    @Mock
    private PostInteractionUserMapper postInteractionUserMapper;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setUser(new User(1L));
        post.setContent("hello world");
        post.setPrivacy(Privacy.PUBLIC);
        post.setCreatedTime(LocalDateTime.now());
        post.setUpdatedTime(LocalDateTime.now());

        PostLike like = new PostLike();
        like.setId(1L);
        like.setPost(post);
        like.setUser(new User(1L));
        like.setCreatedTime(LocalDateTime.now());

        PostImage image = new PostImage();
        image.setId(1L);
        image.setUrl("url");
        image.setPost(post);

        PostComment comment = new PostComment();
        comment.setId(1L);
        comment.setPost(post);
        comment.setUser(new User(1L));
        comment.setContent("comment");
        comment.setCreatedTime(LocalDateTime.now());
        comment.setUpdatedTime(LocalDateTime.now());
        comment.setDeleteStatus(false);

        postList = Collections.singletonList(post);
    }

    @Test
    void whenMapPostToDto_thenCorrect() {
        // Perform mapping
        CurrentUserPost dto = mapper.toDTO(post);

        // Assertions to validate each field is mapped correctly
        assertEquals(post.getId().toString(), dto.getPostId());
        assertEquals(post.getUser().getId().toString(), dto.getUserId());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getCreatedTime(), dto.getCreatedTime());
        assertEquals(post.getUpdatedTime(), dto.getUpdatedTime());
    }

    @Test
    void whenMapNullPost_thenNull() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void whenMapPostListToDtoList_thenCorrect() {
        List<CurrentUserPost> listDTOs = mapper.toListDTOs(postList);

        // Assertions to validate the list is mapped correctly
        assertNotNull(listDTOs);
        assertEquals(postList.size(), listDTOs.size());
    }
}