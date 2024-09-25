package com.olympus.controller;

import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FriendshipController.class)
@Import(SecurityConfig.class)
class FriendshipControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private IFriendshipService friendshipService;
    @MockBean
    private IUserService userService;
    @MockBean
    private AppValidator appValidator;

    @Test
    @WithMockUser(value = "spring")
    void testGetFriendsList() throws Exception {
        Long userId = 1L;
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        mockMvc.perform(get("/v1/friendship/friends-list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer yourTokenHere"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "spring")
    void testUnFriend() throws Exception {
        Long userId = 1L;
        Long targetId = 2L;
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(appValidator.validateUnFriend(any(UserDetails.class), anyLong())).thenReturn(null);
        mockMvc.perform(delete("/v1/friendship/{targetId}", targetId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer yourTokenHere"))
                .andExpect(status().isOk());
    }
}