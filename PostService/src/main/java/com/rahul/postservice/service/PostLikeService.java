package com.rahul.postservice.service;

import com.rahul.postservice.entity.Post;
import com.rahul.postservice.entity.PostLike;
import com.rahul.postservice.exception.BadRequestException;
import com.rahul.postservice.exception.ResourceNotFoundException;
import com.rahul.postservice.repo.PostLikeRepo;
import com.rahul.postservice.repo.PostRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {
    @Autowired
    private final PostLikeRepo postLikeRepo;
    @Autowired
    private final PostRepo postRepo;
    @Autowired
    private final ModelMapper modelMapper;

    @Transactional
    public void likePost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} like the post with ID: {}", userId, postId);
       postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        boolean hasAlreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if(hasAlreadyLiked){
            throw new BadRequestException("You cannot like the post again");
        }
        PostLike postLike = new PostLike();
        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLikeRepo.save(postLike);

        // to do: send the noti to users;
    }

    @Transactional
    public void unlikPost(Long postId) {
        Long userId = 1L;
        log.info("User with ID: {} unlike the post with ID: {}", userId, postId);
        postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with ID: " + postId));

        boolean hasAlreadyLiked = postLikeRepo.existsByUserIdAndPostId(userId, postId);
        if(!hasAlreadyLiked){
            throw new BadRequestException("You cannot unlike the post that you have not liked it yet!");
        }
        postLikeRepo.deleteByPostIdAndUserId(userId, postId);
    }
}
