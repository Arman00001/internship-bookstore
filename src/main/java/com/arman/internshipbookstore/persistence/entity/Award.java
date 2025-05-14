package com.arman.internshipbookstore.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "award")
public class Award {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "award_id_seq")
    @SequenceGenerator(
            name = "award_id_seq",
            sequenceName = "award_id_seq",
            allocationSize = 50
    )
    private Long id;

    @Column(columnDefinition = "TEXT",name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "award")
    private List<BookAward> bookAwards = new ArrayList<>();

}
