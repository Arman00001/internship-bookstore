package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.dto.author.AuthorCreateDto;
import com.arman.internshipbookstore.service.dto.author.AuthorResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public AuthorResponseDto getAuthor(@PathVariable("id") Long id){
        return authorService.getAuthorResponseById(id);
    }

    @GetMapping("/name")
    public Author getAuthorByName(@RequestParam String name){
        return authorService.getAuthorByName(name);
    }

    @PostMapping
    public AuthorResponseDto addAuthor(@RequestBody @Valid AuthorCreateDto authorCreateDto){
        return authorService.addAuthor(authorCreateDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAuthor(@RequestParam("id") Long id){
        authorService.deleteAuthor(id);
    }


}