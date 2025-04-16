package com.arman.internshipbookstore.persistence.entity;


import com.arman.internshipbookstore.enums.Genre;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_id_seq")
    @SequenceGenerator(
            name = "book_id_seq",
            sequenceName = "book_id_seq",
            allocationSize = 50)
    private Long id;

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
//    @Column(name = "ratings_by_stars")
//    private List<Integer> ratingsByStars;
//    private String ratingsByStars;

    @Column(name = "one_star_ratings")
    private Integer oneStarRatings;

    @Column(name = "two_star_ratings")
    private Integer twoStarRatings;

    @Column(name = "three_star_ratings")
    private Integer threeStarRatings;

    @Column(name = "four_star_ratings")
    private Integer fourStarRatings;

    @Column(name = "five_star_ratings")
    private Integer fiveStarRatings;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAward> bookAwards = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "book_genres", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "genre")
    private Set<Genre> genres;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "book_character",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private Set<Characters> characters = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;


    public void addBookAuthor(Author author, String role){
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setAuthor(author);
        bookAuthor.setBook(this);
        bookAuthor.setRole(role);
        bookAuthors.add(bookAuthor);
        author.getBooks().add(bookAuthor);
    }

    public void addAward(Award award, Integer year){
        BookAward bookAward = new BookAward();
        bookAward.setAward(award);
        bookAward.setYear(year);
        bookAward.setBook(this);
        bookAwards.add(bookAward);
        award.getBookAwards().add(bookAward);
    }


    public void addCharacter(Characters character){
        characters.add(character);
        character.getBooks().add(this);
    }

    public void addStarRatings(Integer[] stars){
        if(stars.length==0){
            oneStarRatings = 0;
            twoStarRatings = 0;
            threeStarRatings = 0;
            fourStarRatings = 0;
            fiveStarRatings = 0;
        }
        else {
            oneStarRatings = stars[0];
            twoStarRatings = stars[1];
            threeStarRatings = stars[2];
            fourStarRatings = stars[3];
            fiveStarRatings = stars[4];
        }
    }

    public String getRatingsByStars(){
        return oneStarRatings+", "+
                twoStarRatings+", "+
                threeStarRatings+", "+
                fourStarRatings+", "+
                fiveStarRatings;
    }
}