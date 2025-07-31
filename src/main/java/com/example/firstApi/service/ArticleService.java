package com.example.firstApi.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.firstApi.dto.ArticleRequestDTO;
import com.example.firstApi.model.Article;
import com.example.firstApi.model.ArticleCategory;
import com.example.firstApi.model.ArticleMedia;
import com.example.firstApi.model.ArticleTag;
import com.example.firstApi.model.Source;
import com.example.firstApi.model.User;
import com.example.firstApi.repository.ArticleCategoryRepository;
import com.example.firstApi.repository.ArticleMediaRepository;
import com.example.firstApi.repository.ArticleRepository;
import com.example.firstApi.repository.ArticleTagRepository;
import com.example.firstApi.repository.SourceRepository;
import com.example.firstApi.repository.UserRepository;
import com.example.firstApi.service.utils.MediaStorageService;
import com.example.firstApi.service.utils.MediaTypeResolver;
import com.example.firstApi.util.SlugUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final SourceRepository sourceRepository;
    private final ArticleTagRepository tagRepository;
    private final ArticleCategoryRepository categoryRepository;
    private final ArticleMediaRepository mediaRepository;

    public Map<String, Object> createArticle(ArticleRequestDTO request) throws Exception {
        // Extract fields
        ArticleRequestDTO.ArticleData article = request.getArticle();
        String sourceId = request.getSourceId();
        String userId = request.getUserId();
        List<ArticleRequestDTO.Tag> tags = request.getTags();
        List<ArticleRequestDTO.Tag> categories = request.getCategories();
        List<String> media = article.getMedia();

        if (userId == null || userId.isEmpty()) {
            userId = userRepository.findFirstBySourceId(sourceId)
                    .map(User::getUserId)
                    .orElseThrow(() -> new RuntimeException("No user found for source"));
        }

        Source source = sourceRepository.findBySourceId(sourceId)
                .orElseThrow(() -> new RuntimeException("Source not found"));

        if (source.getStatus().equals(Source.Status.unverified)) {
            throw new RuntimeException("This account is unverified and cannot post");
        }
        if (source.getStatus().equals(Source.Status.expired)) {
            throw new RuntimeException("This account is expired. Please renew.");
        }

        String articleId = UUID.randomUUID().toString();
        String slug = SlugUtil.generateSlug(article.getTitle());

        Article newArticle = Article.builder()
                .articleId(articleId)
                .userId(userId)
                .sourceId(sourceId)
                .title(article.getTitle())
                .slug(slug)
                .isScheduled(false) // <-- ADD THIS
                .content(article.getContent())
                .mediaLink(article.getMediaLink())
                .isFeatured(article.getIsFeatured())
                .status(Article.Status.draft) // if you're using enum
                .language("en") // or set based on input
                .releaseDate(LocalDate.parse(article.getReleaseDate()))
                .build();

        articleRepository.save(newArticle);

        for (ArticleRequestDTO.Tag tag : tags) {
            tagRepository.save(new ArticleTag(articleId, tag.getLabel(), tag.getValue()));
        }

        for (ArticleRequestDTO.Tag cat : categories) {
            categoryRepository.save(new ArticleCategory(articleId, cat.getLabel(), cat.getValue()));
        }

        for (String base64 : media) {
            String type = MediaTypeResolver.getMediaType(base64);
            String url = MediaStorageService.saveMedia(base64, type); // Mocked now

            mediaRepository.save(new ArticleMedia(articleId, sourceId, userId, url, type));
        }

        return Map.of(
                "status", true,
                "type", "articles.create.success",
                "redirect_url", articleId,
                "message", "Article created successfully and pending approval");
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(String articleId) {
        return articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NoSuchElementException("Article not found"));
    }

    public Map<String, Object> updateArticle(String articleId, ArticleRequestDTO request) {
        Article existing = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NoSuchElementException("Article not found"));

        ArticleRequestDTO.ArticleData data = request.getArticle();
        existing.setTitle(data.getTitle());
        existing.setContent(data.getContent());
        existing.setMediaLink(data.getMediaLink());
        existing.setIsFeatured(data.getIsFeatured());
        existing.setReleaseDate(LocalDate.parse(data.getReleaseDate()));
        existing.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        articleRepository.save(existing);

        return Map.of(
                "status", true,
                "message", "Article updated",
                "data", existing);
    }

    public void deleteArticle(String articleId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new NoSuchElementException("Article not found"));
        articleRepository.delete(article);
    }

}
