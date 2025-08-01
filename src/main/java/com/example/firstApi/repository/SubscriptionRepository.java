package com.example.firstApi.repository;

import com.example.firstApi.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findBySubscriptionId(String subscriptionId);
    void deleteByUserIdAndStatus(String userId, String status);
}
