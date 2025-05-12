package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.dto.AuthorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/name")
    public Author getAuthorByName(@RequestParam String name){
        return authorService.getAuthorByName(name);
    }

    @PostMapping
    public AuthorDto addAuthor(@RequestBody @Valid AuthorDto authorDto){
        return authorService.addAuthor(authorDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAuthor(@RequestParam("id") Long id){
        authorService.delete(id);
    }


}