package com.olympus.service;

import com.olympus.dto.request.PostCreate;
import com.olympus.dto.request.PostUpdate;
import com.olympus.dto.response.OtherUserPost;
import com.olympus.dto.response.curentuserpost.CurrentUserPost;
import com.olympus.dto.response.newsfeed.NewsfeedPostDTO;
import com.olympus.entity.Post;
import com.olympus.utils.CustomPage;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IPostService {
    Long createPost(Long userId, PostCreate postCreate, List<String> imageUrls);
    NewsfeedPostDTO getSingleNewsfeedPost(Long postId);
    Long updatePost(Long postId, PostUpdate createPostReq, List<String> imageUrls);
    boolean existByPostIdAndNotDeleted(Long id);
    boolean existsByIdAndUserId(Long postId, Long userId);
    void deletePost(Long id);
    Post findByPostId(long id);
    Page<NewsfeedPostDTO> getNewsfeed(Long userId, int page, int size);
    CustomPage<NewsfeedPostDTO> getNewsfeedWithCustomPage(Long userId, int page, int size);
    Page<CurrentUserPost> getCurrentUserPosts(Long loggedInUserId, int page, int size);
    CustomPage<CurrentUserPost> getCurrentUserPostsWithCustomPage(Long loggedInUserId, int page, int size);
    Page<OtherUserPost> getFriendPosts(Long userId, int page, int size);
    CustomPage<OtherUserPost> getFriendPostsWithCustomPage(Long userId, int page, int size);
    Page<OtherUserPost> getOtherUserPosts(Long userId, int page, int size);
    CustomPage<OtherUserPost> getOtherUserPostsWithCustomPage(Long userId, int page, int size);

    CurrentUserPost getCurrentUserSpecificPost(Long userId, Long postId);

    OtherUserPost getFriendSpecificPost(Long userId, Long postId);

    OtherUserPost getOtherUserSpecificPost(Long userId, Long postId);
}
