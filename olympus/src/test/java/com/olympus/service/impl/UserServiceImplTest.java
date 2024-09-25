package com.olympus.service.impl;

import com.olympus.dto.request.AccountLogin;
import com.olympus.dto.request.AccountPasswordReset;
import com.olympus.dto.request.AccountRegister;
import com.olympus.dto.request.UserUpdate;
import com.olympus.dto.response.CurrentUserProfile;
import com.olympus.dto.response.OtherUserProfile;
import com.olympus.entity.FriendRequest;
import com.olympus.entity.Friendship;
import com.olympus.entity.User;
import com.olympus.exception.UserNotFoundException;
import com.olympus.mapper.CurrentUserProfileMapper;
import com.olympus.mapper.OtherUserProfileMapper;
import com.olympus.mapper.UserUpdateMapper;
import com.olympus.repository.IUserRepository;
import com.olympus.service.IFriendRequestService;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;
    @Mock
    IUserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserUpdateMapper userUpdateMapper;
    @Mock
    private CurrentUserProfileMapper currentUserProfileMapper;
    @Mock
    private IImageService imageService;
    @Mock
    private IFriendRequestService friendRequestService;
    @Mock
    private IFriendshipService friendshipService;
    @Mock
    private OtherUserProfileMapper otherUserProfileMapper;

    @Test
    public void testFindUserByEmail_EmailExist() {
        // Arrange
        String email = "test@example.com";
        User mockUser = new User(1L); // Setup mock user as needed
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.findUserByEmail(email);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());
    }

    @Test
    void testFindUserByEmail_EmailNotExist() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findUserByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findUserByEmail(email);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByUserDetails_Found() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        User user = new User();
        user.setId(1L);
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        Long userId = userService.findIdByUserDetails(userDetails);

        // Assert
        assertNotNull(userId);
        assertEquals(1L, userId);
    }

    @Test
    void testFindByUserDetails_NotFound() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("user@example.com");
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        Long userId = userService.findIdByUserDetails(userDetails);

        // Assert
        assertNull(userId);
    }

    @Test
    void testRegister() {
        // Arrange
        AccountRegister accountRegister = new AccountRegister();
        accountRegister.setEmail("newuser@example.com");
        accountRegister.setPassword("password");
        User savedUser = new User();
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        Long userId = userService.register(accountRegister);

        // Assert
        assertNotNull(userId);
        assertEquals(1L, userId);
        verify(passwordEncoder).encode("password");
    }

    @Test
    void testExistEmail_Exist() {
        //Arrange
        String email = "user@email.com";
        when(userRepository.existsByEmail(email.trim().toLowerCase())).thenReturn(true);

        //Act
        boolean exist = userService.existsEmail(email);

        //Assert
        assertTrue(exist);
    }

    @Test
    void testExistEmail_NotExist() {
        //Arrange
        String email = "user@email.com";
        when(userRepository.existsByEmail(anyString().trim().toLowerCase())).thenReturn(false);

        //Act
        boolean exist = userService.existsEmail(email);

        //Assert
        assertFalse(exist);
    }

    @Test
    void testExistUserByEmailAndPassword_Exist() {
        // Arrange
        AccountLogin accountLogin = new AccountLogin();
        accountLogin.setEmail("user@example.com");
        accountLogin.setPassword("password");
        User user = new User(1L);
        user.setPassword(passwordEncoder.encode("password"));
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(accountLogin.getPassword(), user.getPassword())).thenReturn(true);

        // Act
        boolean matches = userService.existsUserByEmailAndPassword(accountLogin);

        // Assert
        assertTrue(matches);
    }

    @Test
    void testExistUserByEmailAndPassword_NotExist() {
        // Arrange
        AccountLogin accountLogin = new AccountLogin();
        accountLogin.setEmail("user@example.com");
        accountLogin.setPassword("password");
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        boolean matches = userService.existsUserByEmailAndPassword(accountLogin);

        // Assert
        assertFalse(matches);
    }

    @Test
    void testUpdatePassword_Success() {
        // Arrange
        AccountPasswordReset request = new AccountPasswordReset();
        request.setEmail("user@example.com");
        request.setPassword("newPassword");
        User user = new User(1L); // Setup user
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        // Act
        userService.updatePassword(request);

        // Assert
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("newPassword");
    }

    @Test
    void testUpdatePassword_ThrowExceptionWhenUserNotFound() {
        // Arrange
        AccountPasswordReset request = new AccountPasswordReset();
        request.setEmail("user@example.com");
        request.setPassword("newPassword");
        User user = mock(User.class);
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(user.isDeleteStatus()).thenReturn(true);

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(request));
    }

    @Test
    void testUpdatePassword_WhenUserStatusIsDelete_ThenThrowExceptionWhenUserNotFound() {
        // Arrange
        AccountPasswordReset request = new AccountPasswordReset();
        request.setEmail("user@example.com");
        request.setPassword("newPassword");
        when(userRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());

        // Assert
        assertThrows(UserNotFoundException.class, () -> userService.updatePassword(request));
    }

    @Test
    void testExistByUserId_Exist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        boolean exists = userService.existByUserId(userId);

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testExistByUserId_NotExist() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act
        boolean exists = userService.existByUserId(userId);

        // Assert
        assertFalse(exists);
    }

    @Test
    void testUpdateUser_Success() throws IOException {
        // Arrange
        Long userId = 1L;
        UserUpdate updateUser = new UserUpdate();
        updateUser.setGender("MALE");
        MultipartFile file = mock(MultipartFile.class);
        User user = new User();
        user.setId(userId);
        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(imageService.save(any())).thenReturn("filename.jpg");
        when(imageService.getImageUrl("filename.jpg")).thenReturn("https://example.com/filename.jpg");

        // Act
        Long updatedUserId = userService.updateUser(userId, updateUser, file);

        // Assert
        assertNotNull(updatedUserId);
        verify(userRepository).save(any(User.class));
        verify(userUpdateMapper).updateEntityFromDTO(eq(updateUser), any(User.class));
    }

    @Test
    public void testUpdateUser_ThrowWhenUserNotFound() {
        // Arrange
        Long userId = 1L;
        UserUpdate updateUser = new UserUpdate();
        MultipartFile file = mock(MultipartFile.class);
        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(Exception.class, () -> userService.updateUser(userId, updateUser, file));
    }

    @Test
    public void testGetCurrentUserProfile() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        CurrentUserProfile expectedProfile = new CurrentUserProfile();
        when(userRepository.getReferenceById(userId)).thenReturn(user);
        when(currentUserProfileMapper.toDTO(user)).thenReturn(expectedProfile);

        // Act
        CurrentUserProfile profile = userService.getCurrentUserProfile(userId);

        // Assert
        assertNotNull(profile);
        assertEquals(expectedProfile, profile);
    }

    @Test
    public void testGetOtherUserProfile() {
        // Arrange
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User(); // Set up target user as needed
        OtherUserProfile expectedProfile = new OtherUserProfile(); // Mock expected DTO
        when(userRepository.getReferenceById(targetUserId)).thenReturn(targetUser);
        when(otherUserProfileMapper.toDTO(targetUser)).thenReturn(expectedProfile);

        // Act
        OtherUserProfile profile = userService.getOtherUserProfile(currentUserId, targetUserId);

        // Assert
        assertNotNull(profile);
        assertEquals(expectedProfile, profile);
    }

    @Test
    public void testGetOtherUserProfile_FriendshipExist() {
        // Arrange
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        OtherUserProfile expectedProfile = new OtherUserProfile();
        Friendship friendship = mock(Friendship.class);
        when(friendshipService.existsFriendship(currentUserId, targetUserId)).thenReturn(true);
        when(friendshipService.findByUserIds(currentUserId, targetUserId)).thenReturn(friendship);
        when(userRepository.getReferenceById(targetUserId)).thenReturn(targetUser);
        when(otherUserProfileMapper.toDTO(targetUser)).thenReturn(expectedProfile);

        // Act
        OtherUserProfile profile = userService.getOtherUserProfile(currentUserId, targetUserId);

        // Assert
        assertNotNull(profile);
        assertEquals(expectedProfile, profile);
    }

    @Test
    public void testGetOtherUserProfile_FriendRequestExist() {
        // Arrange
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        OtherUserProfile expectedProfile = new OtherUserProfile();
        FriendRequest friendRequest = mock(FriendRequest.class);
        when(friendRequestService.findByUserIds(currentUserId, targetUserId)).thenReturn(friendRequest);
        when(userRepository.getReferenceById(targetUserId)).thenReturn(targetUser);
        when(otherUserProfileMapper.toDTO(targetUser)).thenReturn(expectedProfile);

        // Act
        OtherUserProfile profile = userService.getOtherUserProfile(currentUserId, targetUserId);

        // Assert
        assertNotNull(profile);
        assertEquals(expectedProfile, profile);
    }
}