package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
}
