package com.arman.internshipbookstore.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@RequiredArgsConstructor
public class AuthorDto {
    private Long id;

    @NotBlank(message = "Author name should not be empty")
    private String name;
}
