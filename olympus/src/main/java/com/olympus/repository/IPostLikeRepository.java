package com.olympus.repository;

import com.olympus.entity.Post;
import com.olympus.entity.PostLike;
import com.olympus.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IPostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUser_IdAndPost_Id(long userId, long postId);

    void deleteByUserAndPost(User user, Post post);

    @Query("select count(l) from PostLike  l where l.post.id IN :listPostIds and l.createdTime > :lastWeek ")
    Integer countNewPostLikesLastWeek(List<Long> listPostIds, LocalDateTime lastWeek);
}
