package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview,Long> {
}
