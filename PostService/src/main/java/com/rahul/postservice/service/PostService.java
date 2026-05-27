package com.rahul.postservice.service;

import com.rahul.postservice.dto.PostCreateRequestDto;
import com.rahul.postservice.dto.PostDto;
import com.rahul.postservice.entity.Post;
import com.rahul.postservice.exception.ResourceNotFoundException;
import com.rahul.postservice.repo.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    @Autowired
    private final PostRepo postRepo;

    private final ModelMapper modelMapper;

    public PostDto createPost(PostCreateRequestDto postCreateRequestDto, Long userId) {
        log.info("Creating post for user with ID: {}", userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepo.save(post);
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting post with ID: {}", postId);
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found " + "with ID: "
                        + postId));
        return modelMapper.map(post, PostDto.class);

    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        log.info("Getting all the posts of the user with ID: {}", userId);
        List<Post> postList = postRepo.findByUserId(userId);
        return postList
                .stream()
                .map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
