package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.AuthorService;
import com.arman.internshipbookstore.service.dto.AuthorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public AuthorDto addAuthor(@RequestBody @Valid AuthorDto authorDto){
        return authorService.addAuthor(authorDto);
    }

    @DeleteMapping("/deleteAuthor")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteAuthor(@RequestParam("id") Long id){
        authorService.delete(id);
    }


}