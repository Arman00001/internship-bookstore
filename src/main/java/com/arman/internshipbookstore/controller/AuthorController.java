package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.criteria.AuthorSearchCriteria;
import com.arman.internshipbookstore.service.dto.PageResponseDto;
import com.arman.internshipbookstore.service.dto.author.AuthorCreateDto;
import com.arman.internshipbookstore.service.dto.author.AuthorResponseDto;
import com.arman.internshipbookstore.service.dto.author.AuthorUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public PageResponseDto<AuthorResponseDto> getAuthors(@ModelAttribute AuthorSearchCriteria authorSearchCriteria){
        return authorService.searchAuthors(authorSearchCriteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDto> addAuthor(@RequestBody @Valid AuthorCreateDto authorCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authorService.addAuthor(authorCreateDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable("id") Long id,
                                                          AuthorUpdateDto authorUpdateDto){
        return ResponseEntity.ok(authorService.updateAuthor(id,authorUpdateDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAuthor(@PathVariable("id") Long id) {
        authorService.deleteAuthor(id);
    }

}