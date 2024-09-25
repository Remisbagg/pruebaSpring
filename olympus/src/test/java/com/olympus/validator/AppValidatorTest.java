package com.olympus.validator;

import com.olympus.dto.request.PostCreate;
import com.olympus.dto.request.PostUpdate;
import com.olympus.entity.Post;
import com.olympus.entity.PostComment;
import com.olympus.entity.Privacy;
import com.olympus.entity.User;
import com.olympus.exception.InvalidImageException;
import com.olympus.exception.ReportCreateException;
import com.olympus.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppValidatorTest {
    @InjectMocks
    private AppValidator appValidator;
    @Mock
    private IUserService userService;
    @Mock
    private IPostService postService;
    @Mock
    private IFriendRequestService friendRequestService;
    @Mock
    private IFriendshipService friendshipService;
    @Mock
    private IPostCommentService postCommentService;

    @Test
    void testValidatePostCreate() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long pathUserId = 1L;
        PostCreate postCreate = new PostCreate();
        MultipartFile[] files = new MultipartFile[]{new MockMultipartFile("file", "test.png", "image/png", new byte[10])};
        when(userService.findIdByUserDetails(any())).thenReturn(pathUserId);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCreate(userDetails, pathUserId, postCreate, files);

        // Assert
        assertNull(result);
    }

    @Test
    void testValidatePostUpdate_allowUpdate() {
        /// Arrange
        UserDetails userDetails = mock(UserDetails.class);
        long pathUserId = 1L;
        long pathPostId = 1L;
        PostUpdate post = mock(PostUpdate.class);
        MockMultipartFile[] files = new MockMultipartFile[0];
        Post existedPost = new Post();
        User postOwner = new User();

        // Set up a scenario where the logged-in user is the post owner
        when(appValidator.checkCommonPostCreateAndUpdate(userDetails, pathUserId, post.getContent(), files))
                .thenReturn(null);
        when(userService.findIdByUserDetails(userDetails)).thenReturn(pathUserId);
        when(postService.findByPostId(pathPostId)).thenReturn(existedPost);
        existedPost.setUser(postOwner);
        postOwner.setId(pathUserId);

        // Act
        ResponseEntity<?> result = appValidator.validatePostUpdate(userDetails, pathUserId, pathPostId, post, files);

        // Assert
        assertNull(result);
    }

    @Test
    void testValidatePostUpdate_NotPostOwner() {
        /// Arrange
        UserDetails userDetails = mock(UserDetails.class);
        long pathUserId = 1L;
        long pathPostId = 1L;
        PostUpdate post = mock(PostUpdate.class);
        MockMultipartFile[] files = new MockMultipartFile[0];
        Post existedPost = new Post();
        User postOwner = new User();

        // Set up a scenario where the logged-in user is the post owner
        when(appValidator.checkCommonPostCreateAndUpdate(userDetails, pathUserId, post.getContent(), files))
                .thenReturn(null);
        when(userService.findIdByUserDetails(userDetails)).thenReturn(2L);
        existedPost.setUser(postOwner);
        postOwner.setId(3L);

        // Act
        ResponseEntity<?> result = appValidator.validatePostUpdate(userDetails, pathUserId, pathPostId, post, files);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void whenLoggedInUserIdDoesNotMatchPathUserId_returnConflict() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        long loggedInUserId = 1L;
        long pathUserId = 2L; // Different from loggedInUserId
        long pathPostId = 1L;
        Post post = new Post();
        User postOwner = new User();
        postOwner.setId(loggedInUserId);
        post.setUser(postOwner);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postService.findByPostId(pathPostId)).thenReturn(post);

        // Act
        ResponseEntity<?> result = appValidator.validatePostDelete(userDetails, pathUserId, pathPostId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void whenLoggedInUserIsNotPostOwner_returnConflict() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        long loggedInUserId = 1L; // Logged-in user
        long pathUserId = 1L; // Same as logged-in user, for this scenario
        long pathPostId = 1L;
        Post post = new Post();
        User postOwner = new User();
        postOwner.setId(2L); // Different from loggedInUserId
        post.setUser(postOwner);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postService.findByPostId(pathPostId)).thenReturn(post);

        // Act
        ResponseEntity<?> result = appValidator.validatePostDelete(userDetails, pathUserId, pathPostId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void whenLoggedInUserIsPostOwnerAndPathUserIdMatches_returnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        long loggedInUserId = 1L;
        long pathUserId = 1L; // Same as loggedInUserId
        long pathPostId = 1L;

        Post post = new Post();
        User postOwner = new User();
        postOwner.setId(loggedInUserId);
        post.setUser(postOwner);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postService.findByPostId(pathPostId)).thenReturn(post);

        // Act
        ResponseEntity<?> result = appValidator.validatePostDelete(userDetails, pathUserId, pathPostId);

        // Assert
        assertNull(result);
    }

    // Tests for validateImgFile(MultipartFile file)
    @Test
    void whenFileIsNull_shouldNotThrowException() {
        // Arrange
        MockMultipartFile file = null;

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateImgFile(file));
    }

    @Test
    void whenFileIsEmpty_shouldNotThrowException() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "", "text/plain", new byte[0]);

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateImgFile(file));
    }

    @Test
    void whenFileIsNotImage_shouldThrowInvalidImageException() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "filename.txt", "text/plain", "Some content".getBytes());

        // Act & Assert
        assertThrows(InvalidImageException.class, () -> appValidator.validateImgFile(file));
    }

    @Test
    void whenFileIsImage_shouldNotThrowException() {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "filename.png", "image/png", "Some content".getBytes());

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateImgFile(file));
    }

    // Tests for validateImgFile(MultipartFile[] files)
    @Test
    void whenFilesArrayIsNull_shouldNotThrowException() {
        // Arrange
        MockMultipartFile[] files = null;

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateImgFile(files));
    }

    @Test
    void whenFilesArrayContainsInvalidFile_shouldThrowInvalidImageException() {
        // Arrange
        MockMultipartFile file1 = new MockMultipartFile("file1", "filename1.png", "image/png", "Some content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "filename2.txt", "text/plain", "Some content".getBytes());
        MockMultipartFile[] files = {file1, file2};

        // Act & Assert
        assertThrows(InvalidImageException.class, () -> appValidator.validateImgFile(files));
    }

    @Test
    void whenFilesArrayContainsAllValidFiles_shouldNotThrowException() {
        // Arrange
        MockMultipartFile file1 = new MockMultipartFile("file1", "filename1.png", "image/png", "Some content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file2", "filename2.jpg", "image/jpeg", "Some content".getBytes());
        MockMultipartFile[] files = {file1, file2};

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateImgFile(files));
    }

    //Testing validateFriendRequestSent
    @Test
    void whenSendingToSelf_shouldReturnBadRequest() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestSent(userDetails, userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void whenFriendshipExists_shouldReturnBadRequest() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long receiverId = 2L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendshipService.existsFriendship(loggedInUserId, receiverId)).thenReturn(true);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestSent(userDetails, receiverId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void whenFriendRequestAlreadyExists_shouldReturnBadRequest() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long receiverId = 2L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendRequestService.existsByUserId(loggedInUserId, receiverId)).thenReturn(true);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestSent(userDetails, receiverId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void whenNoErrors_FriendRequestCanBeSent_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long receiverId = 2L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendshipService.existsFriendship(loggedInUserId, receiverId)).thenReturn(false);
        when(friendRequestService.existsByUserId(loggedInUserId, receiverId)).thenReturn(false);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestSent(userDetails, receiverId);

        // Assert
        assertNull(result);
    }

    //Tes validateFriendRequestDelete
    @Test
    void whenUserDoesNotHaveDeletePermission_shouldReturnForbidden() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long requestId = 100L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendRequestService.isValidDeletePermission(loggedInUserId, requestId)).thenReturn(false);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestDelete(userDetails, requestId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenUserHasDeletePermission_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long requestId = 100L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendRequestService.isValidDeletePermission(loggedInUserId, requestId)).thenReturn(true);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestDelete(userDetails, requestId);

        // Assert
        assertNull(result);
    }

    //Test validateFriendRequestAccept
    @Test
    void whenUserDoesNotHaveAcceptPermission_shouldReturnForbidden() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long requestId = 100L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendRequestService.validAccepter(requestId, loggedInUserId)).thenReturn(false);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestAccept(userDetails, requestId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenUserHasAcceptPermission_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long requestId = 100L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendRequestService.validAccepter(requestId, loggedInUserId)).thenReturn(true);

        // Act
        ResponseEntity<?> result = appValidator.validateFriendRequestAccept(userDetails, requestId);

        // Assert
        assertNull(result);
    }

    //Test validatePostCommentCreate
    @Test
    void testValidatePostCommentCreate() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long pathUserId = 1L;
        Long pathPostId = 1L;
        Long loggedInUserId = 1L;

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        Post mockPost = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(mockPost);
        User mockUser = mock(User.class);
        when(mockPost.getUser()).thenReturn(mockUser);
        when(mockPost.getUser().getId()).thenReturn(2L);

        // Define behavior for validateCommonPostComment if it's part of the service

        // Act
        ResponseEntity<?> response = appValidator.validatePostCommentCreate(userDetails, pathUserId, pathPostId);

        // Assert
        assertNotNull(response);

        // Verify interaction
        verify(userService).findIdByUserDetails(userDetails);
        // Optionally verify interaction with validateCommonPostComment, if it's a method of the same class or another mocked dependency
    }

    //Test validatePostCommentUpdate
    @Test
    void whenCommonPostCommentErrorExists_shouldReturnError() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 1L, pathPostId = 10L, pathCommentId = 20L;
        PostComment comment = mock(PostComment.class);
        User commenter = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getUser()).thenReturn(commenter);
        when(commenter.getId()).thenReturn(loggedInUserId);

        Post post = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(2L);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentUpdate(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void whenLoggedInUserIsNotOriginalCommenter_shouldReturnForbiddenResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long commenterId = 2L; // Different from loggedInUserId
        Long pathUserId = 1L, pathPostId = 10L, pathCommentId = 20L;
        PostComment comment = mock(PostComment.class);
        User commenter = mock(User.class);


        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getUser()).thenReturn(commenter);
        when(commenter.getId()).thenReturn(commenterId);

        Post post = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(1L);
        when(comment.getPost()).thenReturn(post);
        when(comment.getPost().getId()).thenReturn(10L);
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentUpdate(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenUserIsAuthorizedToEditComment_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 1L, pathPostId = 10L, pathCommentId = 20L;
        PostComment comment = mock(PostComment.class);
        User commenter = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getUser()).thenReturn(commenter);
        when(commenter.getId()).thenReturn(loggedInUserId); // Logged-in user is the commenter

        Post post = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(1L);
        when(comment.getPost()).thenReturn(post);
        when(comment.getPost().getId()).thenReturn(10L);
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentUpdate(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNull(result); // Expecting null indicating successful validation
    }

    //Test validatePostCommentDelete
    @Test
    void validatePostCommentDelete_WhenCommonPostCommentErrorExists_shouldReturnError() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 1L, pathPostId = 10L, pathCommentId = 20L;

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);


        Post post = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        PostComment comment = mock(PostComment.class);
        when(postCommentService.findById(anyLong())).thenReturn(comment);
        when(comment.getPost()).thenReturn(post);
        when(comment.getUser()).thenReturn(user);
        when(comment.getUser().getId()).thenReturn(1L);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(2L);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentDelete(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
    }

    @Test
    void whenLoggedInUserIsNeitherPostOwnerNorOriginalCommenter_shouldReturnForbiddenResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long postOwnerId = 2L, commenterId = 3L; // Different from loggedInUserId
        Long pathUserId = 3L, pathPostId = 10L, pathCommentId = 20L;
        PostComment comment = mock(PostComment.class);
        Post post = mock(Post.class);
        User postOwner = mock(User.class);
        User commenter = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getPost()).thenReturn(post);
        when(comment.getUser()).thenReturn(commenter);
        when(post.getUser()).thenReturn(postOwner);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(comment.getPost()).thenReturn(post);
        when(comment.getUser()).thenReturn(user);
        when(comment.getUser().getId()).thenReturn(3L);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(3L);
        when(comment.getPost()).thenReturn(post);
        when(comment.getPost().getId()).thenReturn(pathPostId);
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentDelete(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenUserIsEitherPostOwnerOrOriginalCommenter_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 1L, pathPostId = 10L, pathCommentId = 20L;
        PostComment comment = mock(PostComment.class);
        Post post = mock(Post.class);
        User postOwner = mock(User.class);
        User commenter = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getPost()).thenReturn(post);
        when(comment.getUser()).thenReturn(commenter);
        when(comment.getUser().getId()).thenReturn(loggedInUserId);
        when(post.getUser()).thenReturn(postOwner);

        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(1L);
        when(comment.getPost()).thenReturn(post);
        when(comment.getPost().getId()).thenReturn(10L);
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC);

        // Act
        ResponseEntity<?> result = appValidator.validatePostCommentDelete(userDetails, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNull(result); // Expecting null indicating successful validation
    }

    //Test validateLikeOrUnlikeAction
    @Test
    void validateLikeOrUnlikeAction_WhenCommonPostCommentErrorExists_shouldReturnError() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 1L;
        Long pathPostId = 10L;

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        Post post = mock(Post.class);
        when(postService.findByPostId(anyLong())).thenReturn(post);
        User user = mock(User.class);
        when(post.getUser()).thenReturn(user);
        when(post.getUser().getId()).thenReturn(2L);

        // Act
        ResponseEntity<?> result = appValidator.validateLikeOrUnlikeAction(userDetails, pathUserId, pathPostId);

        // Assert
        assertNotNull(result); // Expecting null indicating successful validation
    }

    //Test validateNewsfeed
    @Test
    void validateNewsfeed_whenUserIdsMatch_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L; // Simulating the same user
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act
        ResponseEntity<?> result = appValidator.validateNewsfeed(userDetails, userId);

        // Assert
        assertNull(result); // Expecting null indicating authorization is successful
    }

    @Test
    void validateNewsfeed_whenUserIdsDoNotMatch_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L; // Simulating logged-in user
        Long pathUserId = 2L; // Simulating a different user id from the path
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        // Act
        ResponseEntity<?> result = appValidator.validateNewsfeed(userDetails, pathUserId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode()); // Expecting conflict response
    }

    //Test validateUserUpdate
    @Test
    void whenUserIdsMatch_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L; // Simulating the same user
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act
        ResponseEntity<?> result = appValidator.validateUserUpdate(userDetails, userId);

        // Assert
        assertNull(result); // Expecting null indicating authorization is successful
    }

    @Test
    void whenUserIdsDoNotMatch_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L; // Simulating logged-in user
        Long pathUserId = 2L; // Simulating a different user id from the path
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        // Act
        ResponseEntity<?> result = appValidator.validateUserUpdate(userDetails, pathUserId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode()); // Expecting conflict response
    }

    //Test validateReportCreate
    @Test
    void validateReportCreate_WhenUserIdsMatch_shouldNotThrowException() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L; // Simulating the same user
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act & Assert
        assertDoesNotThrow(() -> appValidator.validateReportCreate(userDetails, userId)); // Expecting no exception
    }

    @Test
    void validateReportCreate_WhenUserIdsDoNotMatch_shouldThrowReportCreateException() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L; // Simulating logged-in user
        Long pathUserId = 2L; // Simulating a different user id from the path
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        // Act & Assert
        assertThrows(ReportCreateException.class,
                () -> appValidator.validateReportCreate(userDetails, pathUserId)); // Expecting ReportCreateException
    }

    //Test validateUnFriend
    @Test
    void whenUserIdsMatch_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L; // Simulating the same user
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act
        ResponseEntity<?> result = appValidator.validateUnFriend(userDetails, userId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode()); // Expecting conflict response
    }

    @Test
    void whenNoFriendshipExists_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long targetUserId = 2L; // Simulating a different user id
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendshipService.existsFriendship(loggedInUserId, targetUserId)).thenReturn(false);

        // Act
        ResponseEntity<?> result = appValidator.validateUnFriend(userDetails, targetUserId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode()); // Expecting conflict response
    }

    @Test
    void whenFriendshipExists_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long targetUserId = 2L;
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);
        when(friendshipService.existsFriendship(loggedInUserId, targetUserId)).thenReturn(true);

        // Act
        ResponseEntity<?> result = appValidator.validateUnFriend(userDetails, targetUserId);

        // Assert
        assertNull(result); // Expecting null indicating successful validation
    }

    //Test validateGetSpecificPost
    @Test
    void validateGetSpecificPost_WhenPostUserIdDoesNotMatchProvidedUserId_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedUserId = 1L;
        Long userId = 2L; // Different from post owner
        Long postId = 10L;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedUserId);
        when(postService.findByPostId(postId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(3L); // Different from userId to trigger conflict

        // Act
        ResponseEntity<?> result = appValidator.validateGetSpecificPost(userDetails, userId, postId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void validateGetSpecificPost_WhenPostIsForFriendsOnlyAndUserIsNotFriend_shouldReturnForbiddenResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedUserId = 1L;
        Long userId = 1L; // Same as logged in user
        Long postId = 10L;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedUserId);
        when(postService.findByPostId(postId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(userId); // Same as userId to pass first check
        when(post.getPrivacy()).thenReturn(Privacy.FRIENDS); // Post is for friends only
        when(friendshipService.existsFriendship(loggedUserId, userId)).thenReturn(false); // User is not a friend

        // Act
        ResponseEntity<?> result = appValidator.validateGetSpecificPost(userDetails, userId, postId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void validateGetSpecificPost_WhenUserIsAuthorizedToViewPost_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedUserId = 1L;
        Long userId = 1L; // Same as logged in user
        Long postId = 10L;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedUserId);
        when(postService.findByPostId(postId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(userId); // Same as userId to pass first check
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC); // Post is public

        // Act
        ResponseEntity<?> result = appValidator.validateGetSpecificPost(userDetails, userId, postId);

        // Assert
        assertNull(result); // Expecting null indicating successful authorization
    }

    //Test validateCommonPostComment
    @Test
    void whenUserIdDoesNotMatchPostOwnerId_shouldReturnConflictResponse() {
        // Arrange
        Long loggedInUserId = 1L;
        Long pathUserId = 1L; // Matches logged-in user
        Long pathPostId = 10L;
        Long pathCommentId = null; // For this test, comment ID is not relevant
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(postService.findByPostId(pathPostId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(2L); // Different from pathUserId to trigger conflict

        // Act
        ResponseEntity<?> result = appValidator.validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void whenPathPostIdDoesNotMatchCommentPostId_shouldReturnConflictResponse() {
        // Arrange
        Long loggedInUserId = 1L;
        Long pathUserId = 1L;
        Long pathPostId = 10L;
        Long pathCommentId = 20L; // Simulating that comment exists
        Post post = new Post();
        post.setUser(new User(1L));
        PostComment comment = mock(PostComment.class);
        Post commentPost = mock(Post.class); // Post associated with the comment
        post.setUser(new User(1L));

        when(postService.findByPostId(pathPostId)).thenReturn(post);
        when(postCommentService.findById(pathCommentId)).thenReturn(comment);
        when(comment.getPost()).thenReturn(commentPost);
        when(commentPost.getId()).thenReturn(11L); // Different from pathPostId

        // Act
        ResponseEntity<?> result = appValidator.validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void whenPostIsPrivateAndUserIsNotOwner_shouldReturnForbiddenResponse() {
        // Arrange
        Long loggedInUserId = 1L;
        Long pathUserId = 2L;
        Long pathPostId = 10L;
        Long pathCommentId = null;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(postService.findByPostId(pathPostId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(2L); // Assume 2L is the ID of a different user
        when(post.getPrivacy()).thenReturn(Privacy.PRIVATE); // Assume the post is private

        // Act
        ResponseEntity<?> result = appValidator.validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenPostIsForFriendsOnlyAndUserIsNotFriend_shouldReturnForbiddenResponse() {
        // Arrange
        Long loggedInUserId = 1L;
        Long pathUserId = 2L;
        Long pathPostId = 10L;
        Long pathCommentId = null;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(postService.findByPostId(pathPostId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(2L); // Different from loggedInUserId to indicate not the owner
        when(post.getPrivacy()).thenReturn(Privacy.FRIENDS);
        when(friendshipService.existsFriendship(loggedInUserId, 2L)).thenReturn(false); // User is not a friend

        // Act
        ResponseEntity<?> result = appValidator.validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
    }

    @Test
    void whenNoErrors_shouldReturnNull() {
        // Arrange
        Long loggedInUserId = 1L;
        Long pathUserId = 1L;
        Long pathPostId = 10L;
        Long pathCommentId = null;
        Post post = mock(Post.class);
        User postOwner = mock(User.class);

        when(postService.findByPostId(pathPostId)).thenReturn(post);
        when(post.getUser()).thenReturn(postOwner);
        when(postOwner.getId()).thenReturn(loggedInUserId); // Same as loggedInUserId to pass checks
        when(post.getPrivacy()).thenReturn(Privacy.PUBLIC); // Post is public

        // Act
        ResponseEntity<?> result = appValidator.validateCommonPostComment(loggedInUserId, pathUserId, pathPostId, pathCommentId);

        // Assert
        assertNull(result); // Expecting null indicating successful validation
    }

    //Test checkCommonPostCreateAndUpdate
    @Test
    void checkCommonPostCreateAndUpdate_WhenFilesAndPostAreNull_shouldReturnBadRequestResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long pathUserId = 1L;

        // Act
        ResponseEntity<?> result = appValidator.checkCommonPostCreateAndUpdate(userDetails, pathUserId, null, null);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    void checkCommonPostCreateAndUpdate_WhenUserIdsDoNotMatch_shouldReturnConflictResponse() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long loggedInUserId = 1L;
        Long pathUserId = 2L; // Different from logged-in user
        Object post = new Object(); // Simulating post content
        when(userService.findIdByUserDetails(userDetails)).thenReturn(loggedInUserId);

        // Act
        ResponseEntity<?> result = appValidator.checkCommonPostCreateAndUpdate(userDetails, pathUserId, post, null);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
    }

    @Test
    void checkCommonPostCreateAndUpdate_WhenValidWithContentOrFiles_shouldReturnNull() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        Long userId = 1L;
        Object post = new Object(); // Simulating post content
        MockMultipartFile[] files = new MockMultipartFile[]{
                new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes())
        };
        when(userService.findIdByUserDetails(userDetails)).thenReturn(userId);

        // Act & Assert for content
        ResponseEntity<?> resultWithContent = appValidator.checkCommonPostCreateAndUpdate(userDetails, userId, post, null);
        assertNull(resultWithContent);

        // Act & Assert for files
        ResponseEntity<?> resultWithFiles = appValidator.checkCommonPostCreateAndUpdate(userDetails, userId, null, files);
        assertNull(resultWithFiles);
    }
}