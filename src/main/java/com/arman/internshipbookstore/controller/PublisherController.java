package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherRepository publisherRepository;

    @GetMapping("/getPublisherByName")
    public Publisher getPublisherByName(@RequestParam String name){
        return publisherRepository.getPublisherByName(name);
    }

    @PostMapping("/addPublisher")
    public void addPublisher(@RequestBody Publisher publisher){
        publisherRepository.save(publisher);
    }
}
