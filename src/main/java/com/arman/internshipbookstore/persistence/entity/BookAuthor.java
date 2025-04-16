package com.arman.internshipbookstore.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "book_author")
public class BookAuthor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_author_id_seq")
    @SequenceGenerator(
            name = "book_author_id_seq",
            sequenceName = "book_author_id_seq",
            allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "author_roles")
    private String role;
}
