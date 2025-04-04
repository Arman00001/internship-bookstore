package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Genre;
import com.arman.internshipbookstore.persistence.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/getGenreByName")
    public Genre getPublisherByName(@RequestParam String name){
        return genreRepository.getGenreByName(name);
    }

    @PostMapping("/addGenre")
    public void addPublisher(@RequestBody Genre genre){
        genreRepository.save(genre);
    }
}
