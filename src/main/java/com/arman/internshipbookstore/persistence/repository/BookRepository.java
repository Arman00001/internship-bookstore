package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.criteria.BookSearchCriteria;
import com.arman.internshipbookstore.service.dto.book.BookResponseDto;
import com.arman.internshipbookstore.service.dto.book.BookSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("""
                SELECT DISTINCT b.id
                FROM Book b
                LEFT JOIN b.publisher p
                LEFT JOIN b.bookAuthors ba
                LEFT JOIN ba.author a
                LEFT JOIN b.genres g
                LEFT JOIN b.bookAwards baw
                LEFT JOIN baw.award aw
                WHERE (:#{#criteria.title} IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%',:#{#criteria.title},'%')))
                AND(:#{#criteria.isbn} IS NULL OR b.isbn = :#{#criteria.isbn})
                AND(:#{#criteria.publisher} IS NULL OR LOWER(p.name) = LOWER(:#{#criteria.publisher}))
                AND(:#{#criteria.authorName} IS NULL OR LOWER(a.name) = LOWER(:#{#criteria.authorName}))
                AND(:#{#criteria.genres} IS NULL OR g IN :#{#criteria.genres})
                AND(:#{#criteria.award} IS NULL OR LOWER(aw.name) LIKE LOWER(CONCAT('%',:#{#criteria.award},'%')))
                AND(:#{#criteria.rating} IS NULL OR b.rating = :#{#criteria.rating})
                AND(:#{#criteria.ratingAbove} IS NULL OR b.rating >:#{#criteria.ratingAbove})
""")
    List<Long> findAllCriteriaIds(BookSearchCriteria criteria);

    @Query("""
                SELECT new com.arman.internshipbookstore.service.dto.book.BookSummaryResponseDto(
                    b.id,
                    b.title,
                    b.imagePath
                ) FROM Book b WHERE b.id IN :ids
""")
    Page<BookSummaryResponseDto> findAllById(List<Long> ids, Pageable pageable);

    @Query("""
            SELECT new com.arman.internshipbookstore.service.dto.book.BookResponseDto(b)
            FROM Book b WHERE b.id=:book_id
            """)
    BookResponseDto getBookResponseById(Long book_id);

    Book getBookById(Long id);

    Book getBookByIsbn(Long isbn);

    @Query("SELECT b.isbn FROM Book b")
    Set<Long> findAllIsbn();

    @Query("SELECT b FROM Book b WHERE b.imagePath LIKE 'Download%'")
    Set<Book> findAllWithoutImageDownloaded();
}
