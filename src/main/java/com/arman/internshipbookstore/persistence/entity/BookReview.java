package com.arman.internshipbookstore.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "book_review")
public class BookReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    @Column(name = "review", nullable = false, columnDefinition = "TEXT")
    private String review;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "posted")
    private LocalDate postedOn;

    @Column(name = "edited")
    private LocalDate editedOn;

}
