package com.rahul.postservice.repo;

import com.rahul.postservice.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepo extends JpaRepository<PostLike, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    void deleteByPostIdAndUserId(Long userId, Long postId);
}
