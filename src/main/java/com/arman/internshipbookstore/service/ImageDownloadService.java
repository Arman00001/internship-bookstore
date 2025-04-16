package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.exception.InvalidImageUrlException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class ImageDownloadService {

    public void uploadImage(Book book, String baseDir) throws IOException {
        String imageUrl = book.getImagePath().replaceFirst("Download ","");
        Files.createDirectories(Paths.get(baseDir)); // Ensure it exists
        String thumbnails = Paths.get("images/thumbnails").toAbsolutePath().toString();
        if(isImageUrlValid(imageUrl)){
            String savePath = getSavePathForBook(book, baseDir);
            downloadImage(imageUrl, savePath);
            book.setImagePath(savePath);
            createThumbnail(savePath, getSavePathForBook(book, thumbnails));
            return;
        }

        throw new InvalidImageUrlException("The given image URL is broken: %s".formatted(imageUrl));
    }

    private void downloadImage(String imageUrl, String savePath) throws MalformedURLException, IOException {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(savePath), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private boolean isImageUrlValid(String imageUrl) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
            connection.setRequestMethod("HEAD"); // faster than GET
            connection.setConnectTimeout(3000);
            connection.connect();
            return connection.getResponseCode() == 200;
        } catch (IOException e) {
            return false;
        }
    }

    private String getSavePathForBook(Book book, String baseDir) {
        String firstLetter = book.getTitle().substring(0, 1).toUpperCase();
        String sanitizedTitle = book.getTitle().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");

        Path dir = Paths.get(baseDir, firstLetter);
        dir.toFile().mkdirs(); // ensure folder exists

        return dir.resolve(sanitizedTitle + ".jpg").toString();
    }

    private void createThumbnail(String originalPath, String thumbnailPath) throws IOException {
        Thumbnails.of(new File(originalPath))
                .size(100, 150)
                .toFile(new File(thumbnailPath));
    }
}
