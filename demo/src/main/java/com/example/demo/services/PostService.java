package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.Post;
import com.example.demo.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
        logger.info("PostService initialized");
    }

    /**
     * Создание нового поста
     */
    public Post createPost(Post post) {
        logger.info("Attempting to create new post with title: {}", post.getTitle());
        try {
            post.setCreatedAt(LocalDateTime.now());
            Post savedPost = postRepository.save(post);
            logger.info("Post created successfully with ID: {}", savedPost.getId());
            return savedPost;
        } catch (Exception e) {
            logger.error("Error while creating a new post with title: {}", post.getTitle(), e);
            throw e;
        }
    }

    /**
     * Редактирование поста
     */
    public Post editPost(Long postId, Post updatedPost) {
        logger.info("Attempting to edit post with ID: {}", postId);
        try {
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
        } catch (Exception e) {
            logger.error("Error while editing post with ID: {}", postId, e);
            throw e;
        }
    }

    /**
     * Удаление поста
     */
    public void deletePost(Long postId) {
        logger.info("Attempting to delete post with ID: {}", postId);
        try {
            Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    logger.error("Post not found for ID: {}", postId);
                    return new RuntimeException("Post not found");
                });
            postRepository.delete(post);
            logger.info("Post deleted successfully with ID: {}", postId);
        } catch (Exception e) {
            logger.error("Error while deleting post with ID: {}", postId, e);
            throw e;
        }
    }
}
