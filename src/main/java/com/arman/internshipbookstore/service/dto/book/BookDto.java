package com.arman.internshipbookstore.service.dto.book;

import com.arman.internshipbookstore.enums.Genre;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@RequiredArgsConstructor
public class BookDto {
    private Long id;

    private String title;
    private String format;
    private String publisherName;
    private String language;
    private String authorNames;

    private Set<Genre> genres;

    private String description;
    private String series;
    private Double rating;
    private Integer pages;
    private Long isbn;
    private LocalDate publishDate;
    private LocalDate firstPublishDate;
    private String edition;
    private Integer numRatings;
    private Integer likedPercent;
    private String setting;
    private Integer bbeScore;
    private Integer bbeVotes;
    private Double price;
    private String ratingsByStars;
    private String awards;
    private String imageUrl;
    private String characters;
}