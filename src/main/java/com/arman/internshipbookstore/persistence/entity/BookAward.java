package com.arman.internshipbookstore.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "book_award")
public class BookAward {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_award_id_seq")
    @SequenceGenerator(
            name = "book_award_id_seq",
            sequenceName = "book_award_id_seq",
            allocationSize = 50
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "award_id")
    private Award award;

    @Column(name = "year", nullable = false)
    private Integer year;
}
