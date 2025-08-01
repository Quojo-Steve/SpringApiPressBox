package com.example.firstApi.repository;

import com.example.firstApi.model.PackageDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageDetailRepository extends JpaRepository<PackageDetail, Long> {
    List<PackageDetail> findByPackageId(String packageId);
}
