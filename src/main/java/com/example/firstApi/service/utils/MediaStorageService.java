package com.example.firstApi.service.utils;

import java.util.UUID;

public class MediaStorageService {
    public static String saveMedia(String base64, String type) {
        // TODO: replace with actual logic
        return "https://mock.url/" + UUID.randomUUID() + "." + type;
    }
}

