package com.olympus.utils;

import com.olympus.dto.response.newsfeed.NewsfeedPostDTO;
import com.olympus.entity.Post;
import com.olympus.entity.Privacy;
import com.olympus.entity.User;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IPostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RealTimeMessengerTest {
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private IPostService postService;

    @Mock
    private IFriendshipService friendshipService;

    @InjectMocks
    private RealTimeMessenger realTimeMessenger;

    @Test
    void broadcastPostToFriendsNewsfeed_whenPostExistsAndUserHasFriends_broadcastsToEachFriend() {
        // Arrange
        Long userId = 1L;
        Long newPostId = 2L;
        List<Long> friendIds = Arrays.asList(3L, 4L, 5L);
        NewsfeedPostDTO post = mock(NewsfeedPostDTO.class);

        when(postService.getSingleNewsfeedPost(newPostId)).thenReturn(post);
        when(friendshipService.getListFriendIds(userId)).thenReturn(friendIds);

        // Act
        realTimeMessenger.broadcastPostToFriendsNewsfeed(userId, newPostId);

        // Assert
        for (Long friendId : friendIds) {
            verify(messagingTemplate).convertAndSend("/topic/user." + friendId + ".newPost", post);
        }
    }

    @Test
    void broadcastCommentToPostOwner_whenPostIsPublicOrForFriends_broadcastsToPostOwnerAndFriends() {
        // Arrange
        long postId = 2L;
        long postOwnerId = 1L;
        List<Long> friendIds = Arrays.asList(3L, 4L, 5L);
        Post post = mock(Post.class);
        User user = mock(User.class);
        Privacy privacy = Privacy.PUBLIC;

        when(postService.findByPostId(postId)).thenReturn(post);
        when(post.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(postOwnerId);
        when(post.getPrivacy()).thenReturn(privacy);
        when(friendshipService.getListFriendIds(postOwnerId)).thenReturn(friendIds);

        // Act
        realTimeMessenger.broadcastComment(postId, anyLong());

        // Assert
        verify(messagingTemplate, times(4)).convertAndSend(anyString(), eq(postOwnerId));
    }
}