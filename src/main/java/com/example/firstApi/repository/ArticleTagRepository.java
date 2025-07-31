package com.example.firstApi.repository;

import com.example.firstApi.model.ArticleTag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {
    Optional<ArticleTag> findBySlug(String slug);
    Optional<ArticleTag> findByArticleIdAndName(String articleId, String name);
}