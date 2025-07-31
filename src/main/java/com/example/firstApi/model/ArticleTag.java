package com.example.firstApi.model;

import jakarta.persistence.*;

@Entity
@Table(name = "article_tags")
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "article_id", nullable = false, length = 36)
    private String articleId;

    @Column(nullable = false, length = 255)
    private String slug;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    // Constructors
    public ArticleTag() {} // Default constructor

    public ArticleTag(String articleId, String name, String slug) {
        this.articleId = articleId;
        this.name = name;
        this.slug = slug;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
