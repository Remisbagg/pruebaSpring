package com.olympus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olympus.config.security.AuthDetailsServiceImpl;
import com.olympus.config.security.SecurityConfig;
import com.olympus.config.jwt.JwtProvider;
import com.olympus.dto.request.UserUpdate;
import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.dto.response.OtherUserProfile;
import com.olympus.service.IUserService;
import com.olympus.validator.AppValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProfileController.class)
@Import(SecurityConfig.class)
class ProfileControllerTest {
    @MockBean
    AuthDetailsServiceImpl authDetailsService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtProvider jwtProvider;
    @MockBean
    private AppValidator appValidator;
    @MockBean
    private IUserService userService;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @WithMockUser(value = "spring")
    void testGetUserProfile_getLoggedInUserProfile() throws Exception {
        //Arrange
        Long userId = 1L;
        Long targetId = 1L;
        CurrentUserProfile profile = mock(CurrentUserProfile.class);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.getCurrentUserProfile(anyLong())).thenReturn(profile);

        //Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/profile", targetId)).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "spring")
    void testGetUserProfile_getOtherUserProfile() throws Exception {
        //Arrange
        Long userId = 1L;
        Long targetId = 2L;
        OtherUserProfile profile = mock(OtherUserProfile.class);
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        when(userService.getOtherUserProfile(anyLong(), anyLong())).thenReturn(profile);

        //Act & Assert
        mockMvc.perform(get("/v1/users/{userId}/profile", targetId)).andExpect(status().isOk());

    }

    @Test
    @WithMockUser(value = "spring")
    void testUpdateProfile_Success() throws Exception {
        //Arrange
        Long userId = 1L;
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setGender("MALE");
        MockMultipartFile mockUserUpdate = new MockMultipartFile("user", "", "application/json", asJsonString(userUpdate).getBytes());
        MockMultipartFile mockFile = new MockMultipartFile("file", "update.jpg", "image/jpeg", "update image content".getBytes());
        when(userService.findIdByUserDetails(any(UserDetails.class))).thenReturn(userId);
        when(appValidator.validateUserUpdate(any(), anyLong())).thenReturn(null);
        when(userService.existByUserId(anyLong())).thenReturn(true);
        doNothing().when(appValidator).validateImgFile((MultipartFile) any());
        when(userService.updateUser(eq(userId), any(UserUpdate.class), any(MockMultipartFile.class))).thenReturn(userId);

        //Act & Assert
        mockMvc.perform(multipart("/v1/users/{userId}/profile", userId)
                        .file(mockUserUpdate)
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA)
                        .with(req -> {
                            req.setMethod("PUT");
                            return req;
                        }))
                .andExpect(status().isOk());
    }
}