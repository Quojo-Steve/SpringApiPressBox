package com.example.firstApi.repository;

import com.example.firstApi.model.ArticleCategory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
    Optional<ArticleCategory> findBySlug(String slug);
    Optional<ArticleCategory> findByArticleIdAndName(String articleId, String name);
}