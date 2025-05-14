package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.dto.author.AuthorCreateDto;
import com.arman.internshipbookstore.service.dto.author.AuthorDto;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author mapDtoToAuthor(AuthorCreateDto authorCreateDto) {
        if (authorCreateDto == null) return null;
        Author author = new Author();
        author.setName(authorCreateDto.getName());
        return author;
    }

    public AuthorDto mapToDto(Author author) {
        if (author == null) return null;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(author.getName());
        authorDto.setId(author.getId());

        return authorDto;
    }
}