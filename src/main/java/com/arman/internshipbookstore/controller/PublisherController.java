package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.PublisherService;
import com.arman.internshipbookstore.service.dto.publisher.PublisherDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping("/name")
    public Publisher getPublisherByName(@RequestParam String name){
        return publisherService.getPublisherByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PublisherDto addPublisher(@RequestBody @Valid PublisherDto publisherDto){
        return publisherService.addPublisher(publisherDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePublisher(@RequestParam("id") Long id){
        publisherService.deletePublisher(id);
    }
}
