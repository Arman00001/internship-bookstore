package com.arman.internshipbookstore.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
public class BookSearchCriteria {
    private String title;
    private String author_name;
    private String publisher;
    private String isbn;
    private List<String> genres;
    private String award;
}
