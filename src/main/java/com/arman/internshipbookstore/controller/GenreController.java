package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Genre;
import com.arman.internshipbookstore.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/getGenreByName")
    public Genre getGenreByName(@RequestParam String name){
        return genreService.getGenreByName(name);
    }

    @PostMapping("/addGenre")
    public void addGenre(@RequestBody Genre genre){
        genreService.save(genre);
    }
}
