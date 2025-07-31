package com.example.firstApi.service.utils;

public class MediaTypeResolver {
    public static String getMediaType(String base64) {
        if (base64.contains("image")) return "image";
        if (base64.contains("video")) return "video";
        if (base64.contains("audio")) return "audio";
        return "unknown";
    }
}
