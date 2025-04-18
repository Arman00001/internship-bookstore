package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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

        Set<Book> books = null;
        try {
            books = bookService.findAllWithoutImageDownloaded();
            for (Book book : books) {
                imageDownloadService.downloadImage(book,baseDir);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bookService.saveAll(books);
            downloading = false;
        }
    }

    @Scheduled(fixedDelay = 20000)
    public void resumeDownload(){
        if(!downloading)
            scheduleImageDownloads();
    }
}