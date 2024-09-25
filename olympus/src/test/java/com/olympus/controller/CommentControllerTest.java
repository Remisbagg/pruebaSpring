package com.olympus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.PostCommentCreate;
import com.olympus.dto.request.PostCommentUpdate;
import com.olympus.service.IPostCommentService;
import com.olympus.service.IPostService;
import com.olympus.service.IUserService;
import com.olympus.utils.RealTimeMessenger;
import com.olympus.validator.AppValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@Import(SecurityConfig.class)
class CommentControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IPostCommentService postCommentService;
    @MockBean
    private IUserService userService;
    @MockBean
    private IPostService postService;
    @MockBean
    private RealTimeMessenger messenger;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser(value = "spring")
    void testCreateComment() throws Exception {
        // Arrange
        PostCommentCreate mockComment = new PostCommentCreate();
        mockComment.setContent("Hello World");
        Long userId = 1L;
        Long postId = 1L;
        Long newCommentId = 10L;

        when(userService.existByUserId(userId)).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(postId)).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(postCommentService.createComment(anyLong(), anyLong(), any(PostCommentCreate.class))).thenReturn(newCommentId);
        when(appValidator.validatePostCommentCreate(any(), anyLong(), anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(post("/v1/users/{userId}/posts/{postId}/comments", userId, postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockComment)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "spring")
    void testUpdateComment() throws Exception {
        // Arrange
        PostCommentUpdate mockComment = new PostCommentUpdate();
        mockComment.setContent("Hello World");
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 10L;

        when(userService.existByUserId(userId)).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(postId)).thenReturn(true);
        when(postCommentService.existByIdAndNotDeleted(commentId)).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(postCommentService.updatePostComment(anyLong(), any(PostCommentUpdate.class))).thenReturn(commentId);
        when(appValidator.validatePostCommentUpdate(any(UserDetails.class), anyLong(), anyLong(), anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/v1/users/{userId}/posts/{postId}/comments/{commentId}", userId, postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockComment)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testDeleteComment() throws Exception {
        // Arrange
        PostCommentUpdate mockComment = new PostCommentUpdate();
        mockComment.setContent("Hello World");
        Long userId = 1L;
        Long postId = 1L;
        Long commentId = 10L;

        when(userService.existByUserId(userId)).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(postId)).thenReturn(true);
        when(postCommentService.existByIdAndNotDeleted(commentId)).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(appValidator.validatePostCommentDelete(any(UserDetails.class), anyLong(), anyLong(), anyLong())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(delete("/v1/users/{userId}/posts/{postId}/comments/{commentId}", userId, postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockComment)))
                .andExpect(status().isOk());
    }
}