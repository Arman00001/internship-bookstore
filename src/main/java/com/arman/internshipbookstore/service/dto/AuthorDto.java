package com.arman.internshipbookstore.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
public class AuthorDto {
    private Long id;

    private String name;
}
