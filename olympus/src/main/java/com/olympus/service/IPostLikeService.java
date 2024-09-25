package com.olympus.service;

public interface IPostLikeService {
    boolean existLike(Long userId, Long postId);
    void likeOrUnlike(Long userId, Long postId);
    boolean likeOrUnlikePost(Long userId, Long postId);
}
