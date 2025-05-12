package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.AsyncImageDownloaderService;
import com.arman.internshipbookstore.service.CsvUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/csv")
@RequiredArgsConstructor
public class CsvUploadController {
    private final CsvUploadService csvUploadService;
    private final AsyncImageDownloaderService asyncImageDownloaderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadCSV(@RequestParam("file") MultipartFile file){
        csvUploadService.uploadFile(file);
        asyncImageDownloaderService.scheduleImageDownloads();
    }
}