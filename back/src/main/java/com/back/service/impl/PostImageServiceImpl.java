package com.back.service.impl;

import com.back.entity.Post;
import com.back.entity.PostImage;
import com.back.repository.IPostImageRepository;
import com.back.service.IPostImageService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PostImageServiceImpl implements IPostImageService {
    private final IPostImageRepository postImageRepository;

    @Autowired
    public PostImageServiceImpl(IPostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @Override
    public void save(String imageUrl, Post post) {
        PostImage postImage = new PostImage(imageUrl, post);
        postImageRepository.save(postImage);
    }

    @Override
    public void save(List<String> imageUrls, Post post) {
        for (String url : imageUrls) {
            save(url, post);
        }
    }

    @Override
    public void deleteByPost(Post post) {
        postImageRepository.deleteByPost(post);
    }
}
