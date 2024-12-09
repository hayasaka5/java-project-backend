package com.example.demo.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import com.example.demo.entities.Post;

@Controller
public class PostWebSocketController {

    @MessageMapping("/message")
    @SendTo("/topic/posts")
    public Post sendMessage(Post post) {
        return post;
    }
}
