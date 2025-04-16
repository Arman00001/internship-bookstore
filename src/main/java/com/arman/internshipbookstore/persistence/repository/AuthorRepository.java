package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author getAuthorByName(String name);

    @Query("SELECT a.name FROM Author a")
    Set<String> findAllNames();
}