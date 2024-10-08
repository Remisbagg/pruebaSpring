package com.back.service.impl;

import com.back.model.Post;
import com.back.model.PostLike;
import com.back.model.User;
import com.back.repository.IPostLikeRepository;
import com.back.service.IPostLikeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
public class PostLikeServiceImpl implements IPostLikeService {
    private final IPostLikeRepository postLikeRepository;

    @Autowired
    public PostLikeServiceImpl(IPostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public boolean existLike(Long userId, Long postId) {
        return postLikeRepository.existsByUser_IdAndPost_Id(userId, postId);
    }

    @Override
    public void likeOrUnlike(Long userId, Long postId) {
        User user = new User(userId);
        Post post = new Post(postId);
        if (existLike(userId, postId)) {
            postLikeRepository.deleteByUserAndPost(user, post);
            return;
        }
        PostLike reaction = new PostLike();
        reaction.setPost(post);
        reaction.setUser(user);
        reaction.setCreatedTime(LocalDateTime.now());
        postLikeRepository.save(reaction);
    }

    @Override
    public boolean likeOrUnlikePost(Long userId, Long postId) {
        User user = new User(userId);
        Post post = new Post(postId);
        if (existLike(userId, postId)) {
            postLikeRepository.deleteByUserAndPost(user, post);
            return false;
        }
        PostLike reaction = new PostLike();
        reaction.setPost(post);
        reaction.setUser(user);
        reaction.setCreatedTime(LocalDateTime.now());
        postLikeRepository.save(reaction);
        return true;
    }
}
