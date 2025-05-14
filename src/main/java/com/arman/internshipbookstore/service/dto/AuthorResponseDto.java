package com.arman.internshipbookstore.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorResponseDto {

    private Long id;

    private String name;

    private String role;

    public AuthorResponseDto(Long id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }
}
