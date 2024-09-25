package com.olympus.utils;

import com.olympus.dto.response.curentuserpost.CurrentUserPost;
import com.olympus.dto.response.newsfeed.NewsfeedPostDTO;
import com.olympus.entity.Post;
import com.olympus.entity.PostComment;
import com.olympus.entity.Privacy;
import com.olympus.service.IFriendshipService;
import com.olympus.service.IPostCommentService;
import com.olympus.service.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class RealTimeMessenger {
    private final SimpMessagingTemplate messagingTemplate;
    private final IPostService postService;
    private final IFriendshipService friendshipService;
    private final IPostCommentService postCommentService;

    @Autowired
    public RealTimeMessenger(SimpMessagingTemplate messagingTemplate, IPostService postService,
                             IFriendshipService friendshipService, IPostCommentService postCommentService) {
        this.messagingTemplate = messagingTemplate;
        this.postService = postService;
        this.friendshipService = friendshipService;
        this.postCommentService = postCommentService;
    }

    public void broadcastPostToFriendsNewsfeed(Long userId, Long newPostId) {
        Post post = postService.findByPostId(newPostId);
        Long postOwnerId = post.getUser().getId();
        Map<String, String> message = new HashMap<>();
        message.put("postId", newPostId.toString());
        message.put("userId", postOwnerId.toString());
        if (post.getPrivacy().equals(Privacy.FRIENDS) || post.getPrivacy().equals(Privacy.PUBLIC)) {
            List<Long> friendIds = friendshipService.getListFriendIds(userId);
            if (!friendIds.isEmpty()) {
                for (Long friendId : friendIds) {
                    // Send the post to each friend's newsfeed
                    messagingTemplate.convertAndSend("/topic/user." + friendId + ".newPost", message);
                }
            }
        }
        messagingTemplate.convertAndSend("/topic/user." + userId + ".newPost", message);
    }

    public void broadcastComment(Long postId, Long commentId) {
        Post post = postService.findByPostId(postId);
        Long postOwnerId = post.getUser().getId();

        PostComment comment = postCommentService.findById(commentId);
        Long commentOwnerId = comment.getUser().getId();
        Map<String,String> message = new HashMap<>();
        message.put("postOwnerId",postOwnerId.toString());
        message.put("commentOwnerId",commentOwnerId.toString());

        //Send notification to each friend's newsfeed
        if (post.getPrivacy().equals(Privacy.FRIENDS) || post.getPrivacy().equals(Privacy.PUBLIC)) {
            List<Long> friendIds = friendshipService.getListFriendIds(postOwnerId);
            if (!friendIds.isEmpty()) {
                for (Long friendId : friendIds) {
                    // Send the post to each friend's newsfeed
                    messagingTemplate.convertAndSend("/topic/user." + friendId + ".newComment", message);
                }
            }
        }
        //Sent notification to post owner
        messagingTemplate.convertAndSend("/topic/user." + postOwnerId + ".newComment", message);
    }

    public void broadcastPostLikeAction(Long postId, boolean like, Long likerId) {
        Post post = postService.findByPostId(postId);
        Long postOwnerId = post.getUser().getId();

        String liked = like? "liked" : "unliked";
        Map<String, String> message = new HashMap<>();
        message.put("like",liked);
        message.put("postId",post.getId().toString());
        message.put("postOwnerId",postOwnerId.toString());
        message.put("likerId",likerId.toString());

        //Send notification to each friend's newsfeed
        if (post.getPrivacy().equals(Privacy.FRIENDS) || post.getPrivacy().equals(Privacy.PUBLIC)) {
            List<Long> friendIds = friendshipService.getListFriendIds(postOwnerId);
            if (!friendIds.isEmpty()) {
                for (Long friendId : friendIds) {
                    // Send notification to each friend's newsfeed
                    message.put("userId", friendId.toString());
                    messagingTemplate.convertAndSend("/topic/user." + friendId + ".newPostLike" , message);
                }
            }
            //Send notification to post owner
            message.put("userId", postOwnerId.toString());
            messagingTemplate.convertAndSend("/topic/user." + postOwnerId + ".newPostLike" , message);
        }
    }
}
