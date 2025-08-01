package com.example.firstApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.sql.Timestamp;

@Entity
@Table(name = "subscriptions")
@Data
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_id", nullable = false, length = 45)
    private String subscriptionId;

    @Column(name = "user_id", nullable = false, length = 36)
    private String userId;

    @Column(name = "source_id", nullable = false, length = 36)
    private String sourceId;

    @Column(name = "package_id", nullable = false, length = 36)
    private String packageId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "billing_date", nullable = false)
    private LocalDate billingDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false, length = 45)
    private String cycle;

    @Column(length = 45)
    private String channel;

    @Column(length = 45)
    private String status;

    @Column(nullable = false, columnDefinition = "ENUM('True','False')")
    private String verified;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "created_by", nullable = false, length = 45)
    private String createdBy;

    @Column(name = "updated_by", length = 45)
    private String updatedBy;
}
