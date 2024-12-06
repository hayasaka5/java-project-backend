package com.example.demo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.entities.Subscription;
import com.example.demo.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionService.class);

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
        logger.info("SubscriptionService initialized");
    }

    public Subscription createSubscription(Subscription subscription) {
        logger.info("Attempting to create a subscription for user with ID: {}", subscription.getUser().getId());
        try {
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            logger.info("Subscription created successfully with ID: {}, expiryDate: {}", 
                        savedSubscription.getId(), savedSubscription.getExpiryDate());
            return savedSubscription;
        } catch (Exception e) {
            logger.error("Error while creating subscription for user ID: {}", subscription.getUser().getId(), e);
            throw e;
        }
    }

    public Subscription getSubscription(Long userId) {
        logger.info("Attempting to get subscription information for user ID: {}", userId);
        try {
            Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUser_Id(userId);
            if (subscriptionOpt.isEmpty()) {
                logger.error("Subscription not found for user ID: {}", userId);
                throw new RuntimeException("Subscription not found");
            }
            Subscription subscription = subscriptionOpt.get();
            logger.info("Subscription found successfully with ID: {}, expiryDate: {}", 
                        subscription.getId(), subscription.getExpiryDate());
            return subscription;
        } catch (Exception e) {
            logger.error("Error while fetching subscription for user ID: {}", userId, e);
            throw e;
        }
    }
}
