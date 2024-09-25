package com.olympus.repository;

import com.olympus.entity.Post;
import com.olympus.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPostImageRepository extends JpaRepository<PostImage, Long> {
    void deleteByPost(Post post);
}
