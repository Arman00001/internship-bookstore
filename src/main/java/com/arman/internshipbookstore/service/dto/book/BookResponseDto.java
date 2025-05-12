package com.arman.internshipbookstore.service.dto.book;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.service.dto.AuthorResponseDto;
import com.arman.internshipbookstore.service.dto.AwardResponseDto;
import com.arman.internshipbookstore.service.dto.PublisherResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookResponseDto {
    private Long id;
    private String title;
    private String description;
    private Double rating;

    private String format;
    private String language;
    private Long isbn;

    private List<AuthorResponseDto> authors;
    private List<PublisherResponseDto> publishers;
    private List<Genre> genres;
    private List<AwardResponseDto> awards;

    private String series;
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
    private String imageUrl;
    private String characters;

    private Integer oneStarRatings = 0;

    private Integer twoStarRatings = 0;

    private Integer threeStarRatings = 0;

    private Integer fourStarRatings = 0;

    private Integer fiveStarRatings = 0;

    public BookResponseDto(Long id,
                           String title,
                           String description,
                           Double rating,
                           String format,
                           String language,
                           Long isbn,
                           List<AuthorResponseDto> authors,
                           List<PublisherResponseDto> publishers,
                           List<Genre> genres,
                           List<AwardResponseDto> awards,
                           String series,
                           Integer pages,
                           LocalDate publishDate,
                           LocalDate firstPublishDate,
                           String edition,
                           Integer numRatings,
                           Integer likedPercent,
                           String setting,
                           Integer bbeScore,
                           Integer bbeVotes,
                           Double price,
                           Integer oneStarRatings,
                           Integer twoStarRatings,
                           Integer threeStarRatings,
                           Integer fourStarRatings,
                           Integer fiveStarRatings,
                           String imageUrl,
                           String characters) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.format = format;
        this.language = language;
        this.isbn = isbn;
        this.authors = authors;
        this.publishers = publishers;
        this.genres = genres;
        this.awards = awards;
        this.series = series;
        this.pages = pages;
        this.publishDate = publishDate;
        this.firstPublishDate = firstPublishDate;
        this.edition = edition;
        this.numRatings = numRatings;
        this.likedPercent = likedPercent;
        this.setting = setting;
        this.bbeScore = bbeScore;
        this.bbeVotes = bbeVotes;
        this.price = price;
        this.oneStarRatings = oneStarRatings;
        this.twoStarRatings = twoStarRatings;
        this.threeStarRatings = threeStarRatings;
        this.fourStarRatings = fourStarRatings;
        this.fiveStarRatings = fiveStarRatings;
        this.imageUrl = imageUrl;
        this.characters = characters;
    }
}
