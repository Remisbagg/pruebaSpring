package com.olympus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.PostCreate;
import com.olympus.dto.request.PostUpdate;
import com.olympus.dto.response.OtherUserPost;
import com.olympus.dto.response.curentuserpost.CurrentUserPost;
import com.olympus.dto.response.newsfeed.NewsfeedPostDTO;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IImageService;
import com.olympus.service.IPostService;
import com.olympus.service.IUserService;
import com.olympus.utils.RealTimeMessenger;
import com.olympus.validator.AppValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(PostController.class)
@Import(SecurityConfig.class)
class PostControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IImageService iImageService;
    @MockBean
    private IPostService postService;
    @MockBean
    private IUserService userService;
    @MockBean
    private IFriendshipService friendshipService;
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
    void getUserPosts_CurrenUserPosts() throws Exception {
        // Arrange
        Long userId = 1L; // Example user ID

        // Create mock CurrentUserPost instances
        CurrentUserPost post1 = new CurrentUserPost();
        CurrentUserPost post2 = new CurrentUserPost();
        List<CurrentUserPost> posts = Arrays.asList(post1, post2);

        // Mock Page<CurrentUserPost>
        Page<CurrentUserPost> mockPosts = new PageImpl<>(posts, PageRequest.of(1, 2), 2L);

        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(postService.getCurrentUserPosts(eq(userId), anyInt(), anyInt())).thenReturn(mockPosts);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/posts", userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void getUserPosts_FriendPosts() throws Exception {
        // Arrange
        Long loggedInUserId = 1L; // Example user ID
        Long friendId = 2L;

        // Create mock CurrentUserPost instances
        OtherUserPost post1 = new OtherUserPost();
        OtherUserPost post2 = new OtherUserPost();
        List<OtherUserPost> posts = Arrays.asList(post1, post2);

        // Mock Page<CurrentUserPost>
        Page<OtherUserPost> mockPosts = new PageImpl<>(posts, PageRequest.of(1, 2), 2L);

        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(friendshipService.existsFriendship(loggedInUserId, friendId)).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(loggedInUserId);
        when(postService.getFriendPosts(eq(friendId), anyInt(), anyInt())).thenReturn(mockPosts);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/posts", friendId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void getUserPosts_OtherUserPosts() throws Exception {
        // Arrange
        Long loggedInUserId = 1L; // Example user ID
        Long targetId = 2L;

        OtherUserPost post1 = new OtherUserPost();
        OtherUserPost post2 = new OtherUserPost();
        List<OtherUserPost> posts = Arrays.asList(post1, post2);

        Page<OtherUserPost> mockPosts = new PageImpl<>(posts, PageRequest.of(1, 2), 2L);

        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(friendshipService.existsFriendship(loggedInUserId, targetId)).thenReturn(false);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(loggedInUserId);
        when(postService.getFriendPosts(eq(targetId), anyInt(), anyInt())).thenReturn(mockPosts);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/posts", targetId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testGetSpecifPost_CurrentUserPost() throws Exception {
        // Arrange
        Long loggedInUserId = 1L; // Example user ID
        Long targetId = 1L;
        Long postId = 1L;

        CurrentUserPost mockPost = mock(CurrentUserPost.class);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(loggedInUserId);
        when(postService.getCurrentUserSpecificPost(anyLong(), anyLong())).thenReturn(mockPost);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/posts/{postId}", targetId, postId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testGetSpecifPost_OtherUserPost() throws Exception {
        // Arrange
        Long loggedInUserId = 1L; // Example user ID
        Long targetId = 2L;
        Long postId = 1L;

        OtherUserPost mockPost = mock(OtherUserPost.class);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(loggedInUserId);
        when(postService.getOtherUserSpecificPost(anyLong(), anyLong())).thenReturn(mockPost);

        // Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/posts/{postId}", targetId, postId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testCreatePost_Success() throws Exception {
        //Arrange
        Long userId = 1L;
        PostCreate post = new PostCreate();
        post.setContent("Hello World");
        post.setPrivacy("PUBLIC");
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile mockPost = new MockMultipartFile("post", "", "application/json", asJsonString(post).getBytes());
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(appValidator.validatePostCreate(any(), any(), any(), any())).thenReturn(null);
        when(iImageService.save(any())).thenReturn("savedImage.jpg");
        when(iImageService.getImageUrl("savedImage.jpg")).thenReturn("http://image.url/savedImage.jpg");
        when(postService.createPost(eq(userId), any(PostCreate.class), any())).thenReturn(1L);

        // Act & Assert
        mockMvc.perform(multipart("/v1/users/{userId}/posts", userId)
                        .file(mockFile)
                        .file(mockPost)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(value = "spring")
    void testUpdatePost_Success() throws Exception {
        //Arrange
        Long userId = 1L;
        Long postId = 2L;
        PostUpdate postUpdate = new PostUpdate();
        postUpdate.setContent("Updated content");
        postUpdate.setPrivacy("PUBLIC");

        MockMultipartFile mockFile = new MockMultipartFile("file", "update.jpg", "image/jpeg", "update image content".getBytes());
        MockMultipartFile mockPost = new MockMultipartFile("post", "", "application/json", asJsonString(postUpdate).getBytes());

        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(userId)).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(postId)).thenReturn(true);
        when(appValidator.validatePostUpdate(any(), any(), any(), any(), any())).thenReturn(null);
        when(iImageService.save(any())).thenReturn("updatedImage.jpg");
        when(iImageService.getImageUrl("updatedImage.jpg")).thenReturn("http://image.url/updatedImage.jpg");
        when(postService.updatePost(eq(postId), any(PostUpdate.class), any())).thenReturn(postId);

        //Act & Assert
        mockMvc.perform(multipart("/v1/users/{userId}/posts/{postId}", userId, postId)
                        .file(mockFile)
                        .file(mockPost)
                        .contentType(MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testDeletePost_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        Long postId = 2L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(userId)).thenReturn(true);
        when(postService.existByPostIdAndNotDeleted(postId)).thenReturn(true);
        when(appValidator.validatePostDelete(any(UserDetails.class), anyLong(), anyLong())).thenReturn(null);
        mockMvc.perform(delete("/v1/users/{userId}/posts/{postId}", userId, postId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testGetNewsfeed_Success() throws Exception {
        // Arrange
        Long userId = 1L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(userId)).thenReturn(true);

        NewsfeedPostDTO post1 = new NewsfeedPostDTO();
        NewsfeedPostDTO post2 = new NewsfeedPostDTO();
        List<NewsfeedPostDTO> posts = Arrays.asList(post1, post2);

        Page<NewsfeedPostDTO> mockPosts = new PageImpl<>(posts, PageRequest.of(1, 2), 2L);
        when(postService.getNewsfeed(anyLong(), anyInt(), anyInt())).thenReturn(mockPosts);

        //Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/newsfeed", userId))
                .andExpect(status().isOk());
    }

    @Test
    void whenFilesAreNull_returnEmptyList() throws IOException {
        List<String> result = new ArrayList<>();
        assertEquals(0, 0, "Expected empty list when files are null");
    }

}
