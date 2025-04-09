package com.arman.internshipbookstore.service.mapper;

import com.arman.internshipbookstore.persistence.entity.Author;
import com.arman.internshipbookstore.service.dto.AuthorDto;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {
    public Author mapDtoToAuthor(AuthorDto authorDto){
        if(authorDto==null) return null;
        Author author = new Author();
        author.setName(authorDto.getName());
        return author;
    }

    public AuthorDto mapToDto(Author author){
        if(author==null) return null;
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName(author.getName());
        authorDto.setId(authorDto.getId());

        return authorDto;
    }
}