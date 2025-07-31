package com.example.firstApi.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArticleRequestDTO {
    private ArticleData article;
    private List<Tag> tags;
    private List<Tag> categories;
    private String userId;
    private String sourceId;

    @Data
    public static class ArticleData {
        private String title;
        private String content;
        private Boolean isFeatured;
        private List<String> media;
        private String mediaLink;
        private String releaseDate;
    }

    @Data
    public static class Tag {
        private String label;
        private String value;
    }
}
