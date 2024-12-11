package com.example.demo.services;

import com.example.demo.entities.Post;
import com.example.demo.repositories.PostRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
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

    public void generatePostsReport(String filePath) {
        logger.info("Generating XLS report for all posts");

        List<Post> posts = postRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Posts Report");

            // Header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Title");
            headerRow.createCell(2).setCellValue("Content");
            headerRow.createCell(3).setCellValue("Created At");

            // Data rows
            int rowNum = 1;
            for (Post post : posts) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(post.getId());
                row.createCell(1).setCellValue(post.getTitle());
                row.createCell(2).setCellValue(post.getContent());
                row.createCell(3).setCellValue(post.getCreatedAt().toString());
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            logger.info("XLS report generated successfully at: {}", filePath);
        } catch (IOException e) {
            logger.error("Error generating XLS report: {}", e.getMessage(), e);
            throw new RuntimeException("Error generating XLS report", e);
        }
    }
}