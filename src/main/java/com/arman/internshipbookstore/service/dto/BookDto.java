package com.arman.internshipbookstore.service.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
public class BookDto {
    private String bookId;
    private String title;
    private String description;
    private String series;
    private Double rating;
    private Integer pages;
    private Long isbn;
    private String format;
    private LocalDate publishDate;
    private LocalDate firstPublishDate;
    private String language;
    private String edition;
    private Integer numRatings;
    private Integer likedPercent;
    private String setting;
    private Integer bbeScore;
    private Integer bbeVotes;
    private Double price;
    private String publisherName;
    private String ratingsByStars;
}
