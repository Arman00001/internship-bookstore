package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.service.PublisherService;
import lombok.RequiredArgsConstructor;
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
    public void addPublisher(@RequestBody Publisher publisher){
        publisherService.save(publisher);
    }
}
