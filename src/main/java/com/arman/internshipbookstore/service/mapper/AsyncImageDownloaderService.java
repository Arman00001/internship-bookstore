package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.ImageDownloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AsyncImageDownloaderService {
    private final BookService bookService;
    private final ImageDownloadService imageDownloadService;

    private volatile boolean downloading = false;

    @Value("${book.image.upload-path}")
    private String dir;

    @Async
    public void scheduleImageDownloads() {
        if (downloading) return; // skip if already running
        String baseDir = Paths.get(dir).toAbsolutePath().toString();

        downloading = true;

        try {
            Set<Book> books = bookService.findAllWithoutImageDownloaded();
            for (Book book : books) {
                imageDownloadService.uploadImage(book,baseDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            downloading = false;
        }
    }
}
