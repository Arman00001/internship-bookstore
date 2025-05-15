package com.arman.internshipbookstore.service.dto.author;

import com.arman.internshipbookstore.persistence.entity.Author;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorResponseDto {

    private Long id;

    private String name;

    public AuthorResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AuthorResponseDto getAuthorResponse(Author author){
        return new AuthorResponseDto(author.getId(),author.getName());
    }
}
