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
    public ResponseEntity<PublisherResponseDto> addPublisher(@RequestBody @Valid PublisherCreateDto publisherCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publisherService.addPublisher(publisherCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> updatePublisher(@PathVariable("id") Long id,
                                                                @RequestBody @Valid PublisherUpdateDto publisherUpdateDto){
        return ResponseEntity.ok(publisherService.updatePublisher(id,publisherUpdateDto));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deletePublisher(@RequestParam("id") Long id){
        publisherService.deletePublisher(id);
    }
}
