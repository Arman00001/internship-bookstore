package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.PublisherService;
import com.arman.internshipbookstore.service.dto.publisher.PublisherCreateDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping("/name")
    public ResponseEntity<PublisherResponseDto> getPublisherByName(@RequestParam String name) {
        return ResponseEntity.ok(publisherService.getPublisherByName(name));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponseDto> getPublisherById(@PathVariable("id") Long id){
        return ResponseEntity.ok(publisherService.getPublisherById(id));
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
    public void deletePublisher(@RequestParam("id") Long id) {
        publisherService.deletePublisher(id);
    }
}
