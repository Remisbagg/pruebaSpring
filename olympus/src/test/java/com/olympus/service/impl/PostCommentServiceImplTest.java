package com.olympus.service.impl;

import com.olympus.dto.request.PostCommentCreate;
import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.entity.PostComment;
import com.olympus.mapper.PostCommentCreateMapper;
import com.olympus.mapper.PostCommentUpdateMapper;
import com.olympus.repository.IPostCommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostCommentServiceImplTest {
    @InjectMocks
    private PostCommentServiceImpl postCommentService;
    @Mock
    private IPostCommentRepository postCommentRepository;
    @Mock
    private PostCommentCreateMapper postCommentCreateMapper;
    @Mock
    private PostCommentUpdateMapper postCommentUpdateMapper;

    @Test
    public void testCreateComment() {
        //Arrange
        Long userId = 1L;
        Long postId = 1L;
        PostCommentCreate crtReq = new PostCommentCreate(); // Setup your create request object
        PostComment postComment = new PostComment(); // Setup your post comment object
        postComment.setId(1L); // Set an ID to simulate the saved entity
        when(postCommentCreateMapper.dtoToEntity(crtReq)).thenReturn(postComment);
        when(postCommentRepository.save(postComment)).thenReturn(postComment);

        // Act
        Long createdCommentId = postCommentService.createComment(userId, postId, crtReq);

        // Assert
        assertNotNull(createdCommentId);
    }

    @Test
    public void testUpdatePostComment() {
        // Arrange
        Long commentId = 1L;
        PostCommentUpdate updReq = new PostCommentUpdate(); // Setup your update request object
        PostComment currentComment = new PostComment();
        when(postCommentRepository.getReferenceById(commentId)).thenReturn(currentComment);

        // Act
        Long updatedCommentId = postCommentService.updatePostComment(commentId, updReq);

        // Assert
        assertEquals(commentId, updatedCommentId);
    }

    @Test
    public void testFindById() {
        // Arrange
        Long id = 1L;
        PostComment postComment = new PostComment(); // Setup your post comment object
        when(postCommentRepository.getReferenceById(id)).thenReturn(postComment);

        // Act
        PostComment foundComment = postCommentService.findById(id);

        // Assert
        assertNotNull(foundComment, "Should return a comment for valid ID");
    }

    @Test
    public void testDeleteCmt() {
        // Arrange
        Long id = 1L;
        PostComment comment = new PostComment();
        comment.setDeleteStatus(false);
        when(postCommentRepository.getReferenceById(id)).thenReturn(comment);

        // Act
        postCommentService.deleteCmt(id);

        // Assert
        assertTrue(comment.isDeleteStatus());
        verify(postCommentRepository).save(comment);
    }

    @Test
    public void testExistByIdAndNotDeleted_Exists() {
        // Arrange
        Long id = 1L;
        when(postCommentRepository.existByIdAndNotDeleted(id)).thenReturn(1L);

        // Act
        boolean exists = postCommentService.existByIdAndNotDeleted(id);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistByIdAndNotDeleted_NotExists() {
        // Arrange
        Long id = 1L;
        when(postCommentRepository.existByIdAndNotDeleted(id)).thenReturn(0L);

        // Act
        boolean exists = postCommentService.existByIdAndNotDeleted(id);

        // Assert
        assertFalse(exists);
    }
}
