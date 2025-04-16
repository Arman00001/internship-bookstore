package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.service.dto.AuthorDto;
import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.persistence.repository.AuthorRepository;
import com.arman.internshipbookstore.service.mapper.AuthorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public Author getAuthorByName(String name) {
        Author author = authorRepository.getAuthorByName(name);

        return author;
    }


    public Author save(AuthorDto authorDto) {
        Author author = authorRepository.getAuthorByName(authorDto.getName());
        if(author!=null) return author;

        author = authorMapper.mapDtoToAuthor(authorDto);
        Author author1 = authorRepository.save(author);

        return author1;
    }

    public Set<String> findAllNames() {
        return authorRepository.findAllNames();
    }

    public List<Author> findAll() {
        return authorRepository.findAll();
    }
}
