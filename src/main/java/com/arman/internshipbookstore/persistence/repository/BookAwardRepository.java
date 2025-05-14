package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.BookAward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookAwardRepository extends JpaRepository<BookAward, Long> {
    List<BookAward> getBookAwardsByBook_Id(Long bookId);
}
