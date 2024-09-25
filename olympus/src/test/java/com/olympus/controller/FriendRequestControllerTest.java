package com.olympus.controller;

import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.service.IFriendRequestService;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IUserService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendRequestController.class)
@Import(SecurityConfig.class)
class FriendRequestControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IFriendRequestService friendRequestService;
    @MockBean
    private IFriendshipService friendshipService;
    @MockBean
    private IUserService userService;

    @Test
    @WithMockUser(value = "spring")
    void testGetListRequestReceived() throws Exception {
        //Arrange
        Long userId = 1L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);

        //Act & Assert
        mockMvc.perform(get("/v1/friends/requests/received", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testGetListRequestSent() throws Exception {
        //Arrange
        Long userId = 1L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);

        //Act & Assert
        mockMvc.perform(get("/v1/friends/requests/sent", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testSendFriendRequest_Success() throws Exception {
        //Arrange
        Long userId = 1L;
        Long targetId = 2L;
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(appValidator.validateFriendRequestSent(any(UserDetails.class), anyLong())).thenReturn(null);

        //Act & Assert
        mockMvc.perform(post("/v1/friends/requests/sent/{targetUserId}", targetId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void whenCancelFriendRequest_thenReturnSuccessResponse() throws Exception {
        Long requestId = 1L;
        UserDetails userDetails = mock(UserDetails.class);

        // Simulate the behavior when everything is valid
        when(friendRequestService.existByRequestId(anyLong())).thenReturn(true);
        when(appValidator.validateFriendRequestDelete(userDetails, requestId)).thenReturn(null);

        mockMvc.perform(delete("/v1/friends/requests/{requestId}", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify interactions
        verify(friendRequestService).deleteRequest(requestId);
    }

    //Test acceptFriendRequest
    @Test
    @WithMockUser
    void testAcceptFriendRequest() throws Exception {
        // Arrange
        Long requestId = 1L; // Example request ID
        Long friendshipId = 10L; // Example created friendship ID

        when(friendRequestService.existByRequestId(anyLong())).thenReturn(true);
        when(appValidator.validateFriendRequestAccept(any(), eq(requestId))).thenReturn(null); // No validation error
        when(friendshipService.create(requestId)).thenReturn(friendshipId);

        // Act & Assert
        mockMvc.perform(put("/v1/friends/requests/accept/{requestId}", requestId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify interactions
        verify(friendshipService).create(requestId);
        verify(appValidator).validateFriendRequestAccept(any(), eq(requestId));
    }
}
