package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
}
