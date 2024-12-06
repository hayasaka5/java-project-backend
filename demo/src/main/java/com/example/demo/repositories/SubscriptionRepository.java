package com.example.demo.repositories;

import com.example.demo.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    /**
     * Поиск подписки по ID пользователя через связь user
     */
    Optional<Subscription> findByUser_Id(Long userId);
}
