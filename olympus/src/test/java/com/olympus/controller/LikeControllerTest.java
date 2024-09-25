package com.olympus.controller;

import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.service.IPostLikeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
@Import(SecurityConfig.class)
class LikeControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IPostLikeService postLikeService;
    @MockBean
    private IPostService postService;
    @MockBean
    private IUserService userService;
    @MockBean
    private RealTimeMessenger messenger;

    @Test
    @WithMockUser(value = "spring")
    void testLikeOrUnlike() throws Exception {
        //Arrange
        Long userId = 1L;
        Long postId = 2L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(postService.existByPostIdAndNotDeleted(anyLong())).thenReturn(true);
        when(appValidator.validateLikeOrUnlikeAction(any(UserDetails.class), anyLong(), anyLong())).thenReturn(null);

        //Act & Assert
        mockMvc.perform(post("/v1/users/{userId}/posts/{postId}", userId, postId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}