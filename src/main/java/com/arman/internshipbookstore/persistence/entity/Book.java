package com.arman.internshipbookstore.persistence.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "book")
public class Book {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column(name = "bookId", nullable = false, unique = true)
    private String bookId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", name = "description")
    private String description;

    @Column(name = "series", nullable = false)
    private String series;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "pages",nullable = false)
    private Integer pages;

    @Column(name = "isbn", nullable = false, unique = true)
    private Long isbn;

    @Column(name = "format", nullable = false)
    private String format;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
//    private List<BookAuthor> authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher")
    private Publisher publisher;

    @Column(name = "publish_date", nullable = false)
    private String publishDate;

    @Column(name = "first_publish_date")
    private String firstPublishDate;

    @Column(name = "language")
    private String language;

    @Column(name = "edition")
    private String edition;

    @Column(name = "num_ratings")
    private String numRatings;

    @Column(name = "liked_percent")
    private Integer likedPercent;

    @Column(name = "bbe_score")
    private Integer bbeScore;

    @Column(name = "bbe_votes")
    private Integer bbeVotes;

    @Column(name = "price")
    private Double price;
}