package com.example.firstApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "subscribers")
@Data
public class Subscriber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscriber_id", nullable = false, length = 36)
    private String subscriberId;

    @Column(name = "subscription_id", nullable = false, length = 45)
    private String subscriptionId;

    @Column(name = "package_id", nullable = false, length = 50)
    private String packageId;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "created_by", length = 45)
    private String createdBy;

    @Column(name = "updated_by", length = 45)
    private String updatedBy;
}
