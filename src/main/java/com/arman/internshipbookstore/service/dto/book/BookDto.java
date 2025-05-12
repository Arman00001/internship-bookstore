package com.arman.internshipbookstore.service.dto.book;

import com.arman.internshipbookstore.controller.deserializer.StringToGenreSetDeserializer;
import com.arman.internshipbookstore.enums.Genre;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Book title should not be empty")
    private String title;
    @NotBlank(message = "Book format should not be empty")
    private String format;
    @NotBlank(message = "Book publisher name should not be empty")
    private String publisherName;
    @NotBlank(message = "Book language should not be empty")
    private String language;
    @NotBlank(message = "Book author names should not be empty")
    private String authorNames;

    @JsonDeserialize(using = StringToGenreSetDeserializer.class)
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