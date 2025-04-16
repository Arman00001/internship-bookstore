package com.arman.internshipbookstore.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BookAuthorDto {
    private Long id;
    private Long authorId;
    private Long bookId;
    private String role;
}
