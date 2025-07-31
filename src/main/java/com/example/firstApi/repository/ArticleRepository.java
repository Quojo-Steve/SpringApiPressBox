package com.example.firstApi.repository;

import com.example.firstApi.model.Article;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    Optional<Article> findByArticleId(String articleId);
}