package com.arman.internshipbookstore.service.dto;

import com.arman.internshipbookstore.enums.Genre;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class BookSearchCriteria {
    private String title;
    private String authorName;
    private String publisher;
    private Long isbn;
    private Set<Genre> genres;
    private String award;
    private Double ratingAbove;
    private Double rating;
}
