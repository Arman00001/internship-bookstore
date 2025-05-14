package com.arman.internshipbookstore.service.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorCreateDto {

    @NotBlank(message = "Author name should not be empty")
    private String name;

}
