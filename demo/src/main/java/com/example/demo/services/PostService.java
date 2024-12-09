package com.example.demo.services;

import com.example.demo.entities.Post;
import com.example.demo.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final WebSocketService webSocketService;

    @Autowired
    public PostService(PostRepository postRepository, WebSocketService webSocketService) {
        this.postRepository = postRepository;
        this.webSocketService = webSocketService;
        logger.info("PostService initialized");
    }

    public Post createPost(Post post) {
        logger.info("Attempting to create new post with title: {}", post.getTitle());
        post.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        logger.info("Post created successfully with ID: {}", savedPost.getId());

        // Отправка уведомления через WebSocket
        webSocketService.sendMessageToAll("New post was created ");
        logger.info("Notification sent for new post ID: {}", savedPost.getId());

        return savedPost;
    }

    public Post editPost(Long postId, Post updatedPost) {
        logger.info("Attempting to edit post with ID: {}", postId);

        Post existingPost = postRepository.findById(postId)
            .orElseThrow(() -> {
                logger.error("Post not found for ID: {}", postId);
                return new RuntimeException("Post not found");
            });

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        Post savedPost = postRepository.save(existingPost);
        logger.info("Post edited successfully with ID: {}", savedPost.getId());

        return savedPost;
    }

    public void deletePost(Long postId) {
        logger.info("Attempting to delete post with ID: {}", postId);

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> {
                logger.error("Post not found for ID: {}", postId);
                return new RuntimeException("Post not found");
            });

        postRepository.delete(post);
        logger.info("Post deleted successfully with ID: {}", postId);
    }

    public List<Post> getUserPostsIfSubscribed(Long userId) {
        logger.info("Checking if user with ID: {} has an active subscription", userId);

        if (!checkSubscription(userId)) {
            logger.info("User with ID: {} does not have an active subscription", userId);
            return List.of();
        }

        List<Post> userPosts = postRepository.findAllByAuthorId(userId);
        logger.info("Found {} posts for user with ID: {}", userPosts.size(), userId);

        return userPosts;
    }

    private boolean checkSubscription(Long userId) {
        logger.info("Simulating subscription check for user with ID: {}", userId);
        return true;
    }
}
