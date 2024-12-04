package com.example.demo.repositories;

import com.example.demo.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    // Можно добавить кастомные запросы, если потребуется
}
