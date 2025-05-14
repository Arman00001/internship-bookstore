package com.arman.internshipbookstore.service.dto.author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorOfBookResponseDto {

    private Long id;

    private String name;

    private String role;

    public AuthorOfBookResponseDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
