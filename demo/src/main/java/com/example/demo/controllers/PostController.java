package com.example.demo.controllers;

import com.example.demo.entities.Post;
import com.example.demo.services.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

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
     * Create a new post.
     */
    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with the provided data.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post created successfully", 
                            content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = Post.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
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
     * Edit an existing post.
     */
    @Operation(
            summary = "Edit a post",
            description = "Edits an existing post based on the provided post ID and updated data.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "postId",
                            description = "The unique ID of the post to edit",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Post updated successfully",
                            content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = Post.class))),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
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
     * Delete a post by its ID.
     */
    @Operation(
            summary = "Delete a post",
            description = "Deletes a post identified by the provided post ID.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "postId",
                            description = "The unique ID of the post to delete",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "204", description = "Post deleted successfully"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
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
     * Fetch posts of a specific user if the subscription is active.
     */
    @Operation(
            summary = "Fetch user posts",
            description = "Fetch posts of a user only if their subscription is active.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "userId",
                            description = "The unique ID of the user whose posts are to be fetched",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully fetched posts",
                            content = @Content(mediaType = "application/json", 
                            schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "403", description = "No active subscription found or no posts exist"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
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

    /**
     * Generate a report of posts in XLS format.
     */
    @Operation(
            summary = "Generate posts report",
            description = "Generates a report of all posts in XLS format.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Report generated successfully"),
                    @ApiResponse(responseCode = "500", description = "Error generating report")
            }
    )
    @GetMapping("/report")
    public ResponseEntity<?> generatePostsReport() {
        try {
            String filePath = "posts_report.xlsx";
            logger.info("Generating report for posts at path: {}", filePath);
            postService.generatePostsReport(filePath);
            logger.info("Report generated successfully at path: {}", filePath);
            return ResponseEntity.ok("Report generated successfully at: " + filePath);
        } catch (Exception e) {
            logger.error("Error generating posts report: ", e);
            return ResponseEntity.status(500).body("Error generating posts report");
        }
    }
}
