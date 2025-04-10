package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("""
    SELECT b FROM Book b
    JOIN b.publisher p
    JOIN b.authors ba
    JOIN ba.author a
    JOIN b.genres g
    WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%')) OR :title IS NULL)
      AND (:isbn IS NULL OR b.isbn = :isbn)
      AND (LOWER(p.name) = LOWER(:publisher) OR :publisher IS NULL)
      AND (LOWER(a.name) = LOWER(:authorName) OR :authorName IS NULL)
      AND(:genres IS NULL OR g IN :genres)
""") List<Book> getBooksByCriteria(@Param("title") String title,
                                   @Param("publisher") String publisher,
                                   @Param("genres") Set<Genre> genres,
                                   @Param("isbn") Long isbn,
                                   @Param("authorName") String authorName);

    Book getBookByIsbn(Long isbn);

    Book getBookByBookId(String bookId);

    List<Book> getBooksByTitle(String title);

    List<Book> getBooksByGenres(Genre genre);

    List<Book> getBooksByGenres(Set<Genre> genres);

    List<Book> getBookByPublisher(Publisher publisher);
}
