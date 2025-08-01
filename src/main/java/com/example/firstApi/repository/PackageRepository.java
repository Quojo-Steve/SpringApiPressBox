package com.example.firstApi.repository;

import com.example.firstApi.model.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findByPackageId(String packageId);
    List<Package> findAll();
}
