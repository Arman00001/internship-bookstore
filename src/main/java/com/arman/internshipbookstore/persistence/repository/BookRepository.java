package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
}
