package com.example.firstApi.repository;

import com.example.firstApi.model.Source;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceRepository extends JpaRepository<Source, Long> {
    Optional<Source> findBySourceId(String sourceId); // 👈 This is what you need
}
