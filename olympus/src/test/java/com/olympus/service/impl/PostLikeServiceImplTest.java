package com.olympus.service.impl;

import com.olympus.entity.Post;
import com.olympus.entity.PostLike;
import com.olympus.entity.User;
import com.olympus.repository.IPostLikeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostLikeServiceImplTest {
    @InjectMocks
    private PostLikeServiceImpl postLikeService;
    @Mock
    private IPostLikeRepository postLikeRepository;

    @Test
    public void testExistLike_Exist() {
        //Arrange
        long userId = 1L;
        long postId = 2L;
        when(postLikeRepository.existsByUser_IdAndPost_Id(userId, postId))
                .thenReturn(true);

        //Act
        boolean exist = postLikeService.existLike(1L, 2L);

        //Assert
        assertTrue(exist);
    }

    @Test
    public void testExistLike_NotExist() {
        //Arrange
        long userId = 1L;
        long postId = 2L;
        when(postLikeRepository.existsByUser_IdAndPost_Id(userId, postId))
                .thenReturn(false);

        //Act
        boolean exist = postLikeService.existLike(1L, 2L);

        //Assert
        assertFalse(exist);
    }

    @Test
    public void testLikeOrUnlike_Like() {
        //Arrange
        long userId = 1L;
        long postId = 2L;
        when(postLikeRepository.existsByUser_IdAndPost_Id(userId, postId)).thenReturn(false);

        //Act
        postLikeService.likeOrUnlike(userId, postId);

        // Assert
        // Verify that save was called, indicating a like was added
        verify(postLikeRepository).save(any(PostLike.class));
        // Ensure delete was not called
        verify(postLikeRepository, never()).deleteByUserAndPost(any(User.class), any(Post.class));
    }

    @Test
    public void testLikeOrUnlike_UnlikePost() {
        // Arrange
        long userId = 1L;
        long postId = 1L;
        when(postLikeRepository.existsByUser_IdAndPost_Id(userId, postId)).thenReturn(true);

        // Act
        postLikeService.likeOrUnlike(userId, postId);

        // Assert
        // Verify that delete was called, indicating a like was removed
        verify(postLikeRepository).deleteByUserAndPost(any(User.class), any(Post.class));
        // Ensure save was not called
        verify(postLikeRepository, never()).save(any(PostLike.class));
    }
}