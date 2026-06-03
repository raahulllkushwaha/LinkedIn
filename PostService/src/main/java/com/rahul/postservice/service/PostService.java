package com.rahul.postservice.service;

import com.rahul.postservice.auth.AuthContextHolder;
import com.rahul.postservice.client.ConnectionsServiceClient;
import com.rahul.postservice.dto.PersonDto;
import com.rahul.postservice.dto.PostCreateRequestDto;
import com.rahul.postservice.dto.PostDto;
import com.rahul.postservice.entity.Post;
import com.rahul.postservice.event.PostCreated;
import com.rahul.postservice.exception.ResourceNotFoundException;
import com.rahul.postservice.repo.PostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final ConnectionsServiceClient connectionsServiceClient;
    private final KafkaTemplate<Long, PostCreated> postCreatedKafkaTemplate;


    public PostDto createPost(PostCreateRequestDto postCreateRequestDto) {
        Long userId = AuthContextHolder.getCurrentUserId();
        log.info("Creating post for user with ID: {}", userId);
        Post post = modelMapper.map(postCreateRequestDto, Post.class);
        post.setUserId(userId);
        post = postRepo.save(post);

        List<PersonDto> personDtoList = connectionsServiceClient.getFirstDegreeConnections(userId);

        for(PersonDto person: personDtoList){ //sending notification to each connections
            PostCreated postCreated = PostCreated.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .userId(person.getUserId())
                    .ownerUserId(userId)
                    .build();
            postCreatedKafkaTemplate.send("post_created_topic", postCreated);
        }
        return modelMapper.map(post, PostDto.class);
    }

    public PostDto getPostById(Long postId) {
        log.info("Getting post with ID: {}", postId);
        Long userId = AuthContextHolder.getCurrentUserId();


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
