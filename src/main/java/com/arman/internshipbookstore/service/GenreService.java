package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Genre;
import com.arman.internshipbookstore.persistence.repository.GenreRepository;
import com.arman.internshipbookstore.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre getGenreByName(String name){
        return genreRepository.getGenreByName(name);
    }

    public Genre save(Genre genre){
        genreRepository.save(genre);

        Genre genre1 = genreRepository.getGenreByName(genre.getName());

        return genre1;
    }
}