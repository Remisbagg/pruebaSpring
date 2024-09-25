package com.olympus.service;

import com.olympus.entity.Post;

import java.util.List;

public interface IPostImageService {
    void save(String imageUrl, Post post);

    void save(List<String> imageUrls, Post post);

    void deleteByPost(Post post);
}
