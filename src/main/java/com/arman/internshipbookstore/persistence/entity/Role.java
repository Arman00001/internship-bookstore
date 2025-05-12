package com.arman.internshipbookstore.persistence.entity;

import com.arman.internshipbookstore.enums.RoleName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }
}
