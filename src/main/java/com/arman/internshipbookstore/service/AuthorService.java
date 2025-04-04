package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorRepository authorRepository;

    public Author getAuthorByName(String name) {
        return authorRepository.getAuthorByName(name);
    }

    public Author save(Author author) {
        authorRepository.save(author);

        Author author1 = authorRepository.getAuthorByName(author.getName());

        return author1;
    }
}
