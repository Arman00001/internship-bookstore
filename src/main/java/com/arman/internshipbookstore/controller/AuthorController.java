package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/getAuthorByName")
    public Author getAuthorByName(@RequestParam String name){
        return authorService.getAuthorByName(name);
    }

    @PostMapping("/addAuthor")
    public void addAuthor(@RequestBody AuthorDto authorDto){
        authorService.save(authorDto);
    }
}
