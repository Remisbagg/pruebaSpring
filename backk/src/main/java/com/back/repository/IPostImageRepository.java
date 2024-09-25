package com.back.repository;

import com.back.model.Post;
import com.back.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPost(Post post);
}
