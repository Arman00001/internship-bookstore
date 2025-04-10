package com.arman.internshipbookstore.persistence.entity;


import com.arman.internshipbookstore.enums.Genre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

//edit publish date
//
@Entity
@Setter
@Getter
@Table(name = "book")
public class Book {

    @Id
    @Column(name = "book_id", nullable = false, unique = true)
    private String bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(name = "series")
    private String series;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "pages",nullable = false)
    private Integer pages;

    @Column(name = "isbn", nullable = false, unique = true)
    private Long isbn;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "publish_date", nullable = false)
    private LocalDate publishDate;

    @Column(name = "first_publish_date")
    private LocalDate firstPublishDate;

    @Column(name = "language")
    private String language;

    @Column(name = "edition")
    private String edition;

    @Column(name = "num_ratings", nullable = false)
    private Integer numRatings;

    @Column(name = "liked_percent", nullable = false)
    private Integer likedPercent;

    @Column(columnDefinition = "TEXT",name = "setting")
    private String setting;

    @Column(name = "bbe_score")
    private Integer bbeScore;

    @Column(name = "bbe_votes")
    private Integer bbeVotes;

    @Column(name = "price")
    private Double price;

    @Column(name = "image_path")
    private String imagePath;

//    @ElementCollection
    @Column(name = "ratings_by_stars")
//    private List<Integer> ratingsByStars;
    private String ratingsByStars;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> authors;

    @ManyToMany
    @JoinTable(
            name = "book_award",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "award_id")
    )
    private Set<Award> awards;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    private Set<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "book_character",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Characters> characters;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}