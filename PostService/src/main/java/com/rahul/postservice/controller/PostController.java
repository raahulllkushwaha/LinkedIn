package com.rahul.postservice.controller;

import com.rahul.postservice.auth.AuthContextHolder;
import com.rahul.postservice.dto.PostCreateRequestDto;
import com.rahul.postservice.dto.PostDto;
import com.rahul.postservice.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostCreateRequestDto postCreateRequestDto) {
        PostDto postDto = postService.createPost(postCreateRequestDto);
        return new ResponseEntity<>(postDto, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId) {
        Long userId = AuthContextHolder.getCurrentUserId();
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId) {
        List<PostDto> posts = postService.getAllPostsOfUser(userId);
        return ResponseEntity.ok(posts);
    }
}