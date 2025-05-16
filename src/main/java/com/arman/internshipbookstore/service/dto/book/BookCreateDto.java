package com.arman.internshipbookstore.service.dto.book;

import com.arman.internshipbookstore.controller.deserializer.StringToGenreSetDeserializer;
import com.arman.internshipbookstore.enums.Genre;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class BookCreateDto {

    @NotBlank(message = "Book title should not be empty")
    private String title;

    @NotBlank(message = "Book format should not be empty")
    private String format;

    @NotNull(message = "Book publisher name should not be empty")
    private Long publisherId;

    @NotBlank(message = "Book language should not be empty")
    private String language;

    @NotNull(message = "Book author names should not be empty")
    private Map<String, List<String>> authorIdRoles;

    @NotNull(message = "ISBN must be specified")
    private Long isbn;

    @JsonDeserialize(using = StringToGenreSetDeserializer.class)
    private Set<Genre> genres;

    private String description;
    private String series;
    private Double rating;
    private Integer pages;
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
