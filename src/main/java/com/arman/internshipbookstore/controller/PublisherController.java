package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.PublisherService;
import com.arman.internshipbookstore.service.dto.PublisherDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping("/getPublisherByName")
    public Publisher getPublisherByName(@RequestParam String name){
        return publisherService.getPublisherByName(name);
    }

    @PostMapping("/addPublisher")
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDto addPublisher(@RequestBody @Valid PublisherDto publisherDto){
        return publisherService.addPublisher(publisherDto);
    }

    @DeleteMapping("/deletePublisher")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePublisher(@RequestParam("id") Long id){
        publisherService.deletePublisher(id);
    }
}
