package com.example.firstApi.repository;

import com.example.firstApi.model.ArticleMedia;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, Long> {
    Optional<ArticleMedia> findByArticleIdAndUrl(String articleId, String url);
}