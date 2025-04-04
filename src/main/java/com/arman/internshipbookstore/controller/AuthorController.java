package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/getAuthorByName")
    public Author getPublisherByName(@RequestParam String name){
        return authorRepository.getAuthorByName(name);
    }

    @PostMapping("/addAuthor")
    public void addAuthor(@RequestBody Author author){
        authorRepository.save(author);
    }
}
