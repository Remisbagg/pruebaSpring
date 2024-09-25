package com.back.repository;

import com.back.entity.Post;
import com.back.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPost(Post post);
}
