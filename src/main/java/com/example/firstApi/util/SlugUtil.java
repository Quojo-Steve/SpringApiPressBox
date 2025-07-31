package com.example.firstApi.util;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class SlugUtil {

    // Converts "Article Title!" to "article-title"
    public static String slugify(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    // Generates a unique slug like "article-title-8423"
    public static String generateSlug(String title) {
        String baseSlug = slugify(title);
        int randomSuffix = new Random().nextInt(9000) + 1000; // random 4-digit number
        return baseSlug + "-" + randomSuffix;
    }

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
}
