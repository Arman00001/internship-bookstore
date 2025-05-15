package com.arman.internshipbookstore.service.dto.author;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorUpdateDto {

    @NotBlank
    private String name;

}
