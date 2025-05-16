package com.arman.internshipbookstore.service.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookReviewCreateDto {
    @NotNull
    private Long bookId;

    @NotBlank
    private String review;

    @NotNull
    private Integer rating;
}
