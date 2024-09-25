package com.back.service;

import com.back.model.Post;

import java.util.List;

public interface IPostImageService {
    void save(String imageUrl, Post post);

    void save(List<String> imageUrls, Post post);

    void deleteByPost(Post post);
}
