package com.example.firstApi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashPaymentService {

    private final JdbcTemplate jdbc;

    public void createCashPayment(String userId, double amount, String cycle, String packageId, String plan) {
        String subscriptionId = UUID.randomUUID().toString();
        LocalDate createdAt = LocalDate.now();

        // 1. Get source_id
        String sourceId = jdbc.queryForObject(
            "SELECT source_id FROM users WHERE user_id = ?",
            new Object[]{userId},
            String.class
        );

        if (userId == null || sourceId == null || packageId == null || cycle == null) {
            throw new RuntimeException("Payment metadata is incomplete");
        }

        // 2. Cancel existing active subscription
        jdbc.update("""
            UPDATE subscriptions 
            SET status = 'cancelled', updated_by = 'admin'
            WHERE user_id = ? AND status = 'active'
        """, userId);

        // 3. Calculate billing and expiry dates
        LocalDate billingDate = createdAt;
        LocalDate expiryDate = switch (cycle.toLowerCase()) {
            case "monthly" -> billingDate.plusMonths(1);
            case "annually" -> billingDate.plusYears(1);
            default -> throw new RuntimeException("Invalid cycle value");
        };

        // 4. Insert subscription
        int rows = jdbc.update("""
            INSERT INTO subscriptions 
            (subscription_id, user_id, source_id, package_id, amount, 
             billing_date, expiry_date, cycle, status, verified, 
             channel, created_by, updated_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, subscriptionId, userId, sourceId, packageId, amount,
             billingDate, expiryDate, cycle, "active", "True", "Cash", "admin", "admin");

        if (rows == 0) throw new RuntimeException("Payment not created");

        // 5. Insert subscriber
        int subRows = jdbc.update("""
            INSERT INTO subscribers 
            (subscriber_id, subscription_id, package_id, created_by)
            VALUES (?, ?, ?, ?)
        """, userId, subscriptionId, packageId, userId);

        if (subRows == 0) throw new RuntimeException("Subscriber not created");
    }
}
