package com.arman.internshipbookstore.service.dto.book;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.dto.AuthorResponseDto;
import com.arman.internshipbookstore.service.dto.award.AwardResponseDto;
import com.arman.internshipbookstore.service.dto.character.CharacterResponseDto;
import com.arman.internshipbookstore.service.dto.publisher.PublisherResponseDto;
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

    private Integer oneStarRatings = 0;

    private Integer twoStarRatings = 0;

    private Integer threeStarRatings = 0;

    private Integer fourStarRatings = 0;

    private Integer fiveStarRatings = 0;

    private PublisherResponseDto publisher;
    private List<CharacterResponseDto> characters;
    private List<AuthorResponseDto> authors;
    private List<Genre> genres;
    private List<AwardResponseDto> awards;


    public BookResponseDto(Book book){
        this.id = book.getId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.rating = book.getRating();
        this.format = book.getFormat();
        this.language = book.getLanguage();
        this.isbn = book.getIsbn();
        this.series = book.getSeries();
        this.pages = book.getPages();
        this.publishDate = book.getPublishDate();
        this.firstPublishDate = book.getFirstPublishDate();
        this.edition = book.getEdition();
        this.numRatings = book.getNumRatings();
        this.likedPercent = book.getLikedPercent();
        this.setting = book.getSetting();
        this.bbeScore = book.getBbeScore();
        this.bbeVotes = book.getBbeVotes();
        this.price = book.getPrice();
        this.oneStarRatings = book.getOneStarRatings();
        this.twoStarRatings = book.getTwoStarRatings();
        this.threeStarRatings = book.getThreeStarRatings();
        this.fourStarRatings = book.getFourStarRatings();
        this.fiveStarRatings = book.getFiveStarRatings();
        this.imageUrl = book.getImagePath();
        this.genres = List.copyOf(book.getGenres());
        this.characters = CharacterResponseDto.getCharacterResponseList(book.getCharacters());
        this.publisher = PublisherResponseDto.getPublisherResponse(book.getPublisher());
    }


    public BookResponseDto(Long id,
                           String title,
                           String description,
                           Double rating,
                           String format,
                           String language,
                           Long isbn,
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
                           String imageUrl){
        this.id = id;
        this.title = title;
        this.description = description;
        this.rating = rating;
        this.format = format;
        this.language = language;
        this.isbn = isbn;
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
    }
}
