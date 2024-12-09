package com.example.demo.controllers;

import com.example.demo.entities.Post;
import com.example.demo.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    /**
     * Создание нового поста
     */
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        try {
            logger.info("Attempting to create a new post with title: {}", post.getTitle());
            Post createdPost = postService.createPost(post);
            logger.info("Post created successfully with ID: {}", createdPost.getId());
            return ResponseEntity.ok(createdPost);
        } catch (Exception e) {
            logger.error("Error creating a post: ", e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Редактирование поста
     */
    @PutMapping("/{postId}")
    public ResponseEntity<Post> editPost(@PathVariable Long postId, @RequestBody Post updatedPost) {
        try {
            logger.info("Attempting to edit post with ID: {}", postId);
            Post post = postService.editPost(postId, updatedPost);
            logger.info("Post edited successfully with ID: {}", post.getId());
            return ResponseEntity.ok(post);
        } catch (Exception e) {
            logger.error("Error editing the post with ID: {}", postId, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Удаление поста
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        try {
            logger.info("Attempting to delete post with ID: {}", postId);
            postService.deletePost(postId);
            logger.info("Post deleted successfully with ID: {}", postId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting the post with ID: {}", postId, e);
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Получение постов пользователя, если подписка активна
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable Long userId) {
        try {
            logger.info("Fetching posts for user with ID: {}", userId);
            List<Post> posts = postService.getUserPostsIfSubscribed(userId);

            if (posts.isEmpty()) {
                logger.info("No active subscription or no posts found for user with ID: {}", userId);
                return ResponseEntity.status(403).body(posts);
            }

            logger.info("Returning {} posts for user with active subscription", posts.size());
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            logger.error("Error fetching posts for user with ID: {}", userId, e);
            return ResponseEntity.status(500).build();
        }
    }
}
