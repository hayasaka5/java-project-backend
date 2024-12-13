package com.example.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.Subscription;
import com.example.demo.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Operation(
            summary = "Create a new subscription",
            description = "Creates a new subscription using the provided subscription data from the request body.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription createdSubscription = subscriptionService.createSubscription(subscription);
        return ResponseEntity.ok(createdSubscription);
    }

    @Operation(
            summary = "Retrieve user subscription",
            description = "Fetch the subscription details for a specific user by their user ID.",
            parameters = {
                    @io.swagger.v3.oas.annotations.Parameter(
                            name = "userId",
                            description = "The unique ID of the user whose subscription is to be retrieved",
                            required = true
                    )
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Subscription.class))),
                    @ApiResponse(responseCode = "404", description = "No subscription found for the provided user ID"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/{userId}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable Long userId) {
        Subscription subscription = subscriptionService.getSubscription(userId);
        return ResponseEntity.ok(subscription);
    }
}
