package com.example.demo.services;

import com.example.demo.entities.Subscription;
import com.example.demo.repositories.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    // Создание подписки
    public Subscription createSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    // Получение информации о подписке
    public Subscription getSubscription(Long userId) {
        return subscriptionRepository.findById(userId).orElseThrow(() -> new RuntimeException("Subscription not found"));
    }
}
