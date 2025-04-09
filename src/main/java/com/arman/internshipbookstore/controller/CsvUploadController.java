package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.CsvUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class CsvUploadController {
    private final CsvUploadService csvUploadService;

    @PostMapping("/uploadCSV")
    @ResponseStatus(HttpStatus.CREATED)
    public void uploadCSV(@RequestParam("file") MultipartFile file){
        csvUploadService.uploadFile(file);
    }
}