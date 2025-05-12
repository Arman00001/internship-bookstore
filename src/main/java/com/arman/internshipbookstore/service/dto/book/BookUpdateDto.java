package com.arman.internshipbookstore.service.dto.book;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BookUpdateDto {
    @NotNull(message = "Identifier should be given")
    private Long id;
    private String description;
    private Integer starRating;
    private String award;
    private String character;

}