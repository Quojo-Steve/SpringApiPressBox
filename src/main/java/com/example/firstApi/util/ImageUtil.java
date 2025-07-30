package com.example.firstApi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.UUID;

public class ImageUtil {
    public static String saveImageToFile(String base64Data, String directory) throws Exception {
        String folderPath = "./public/" + directory;
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String base64 = base64Data.replaceFirst("^data:image/\\w+;base64,", "");
        byte[] decodedBytes = Base64.getDecoder().decode(base64);

        String fileName = UUID.randomUUID().toString() + ".jpg";
        File file = new File(folderPath + "/" + fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(decodedBytes);
        }

        // URL path (adjust BASE_URL to match your server)
        return "/logos/" + fileName;
    }
}
