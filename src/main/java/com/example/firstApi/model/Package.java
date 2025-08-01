package com.example.firstApi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "packages")
@Getter
@Setter
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "package_id", nullable = false, length = 36)
    private String packageId;

    private String name;

    @Column(name = "sub_heading")
    private String subHeading;

    @Column(name = "price_monthly")
    private BigDecimal priceMonthly;

    @Column(name = "price_annually")
    private BigDecimal priceAnnually;

    private String currency;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Transient
    private List<PackageDetail> details;  // Mapped manually in service
}
