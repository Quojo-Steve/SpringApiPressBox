package com.example.firstApi.repository;

import com.example.firstApi.model.SourceSocial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SourceSocialRepository extends JpaRepository<SourceSocial, Long> {
    Optional<SourceSocial> findBySourceId(String sourceId);
}
