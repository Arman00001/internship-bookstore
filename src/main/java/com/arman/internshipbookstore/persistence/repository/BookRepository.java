package com.arman.internshipbookstore.persistence.repository;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("""
    SELECT DISTINCT b FROM Book b
    JOIN b.publisher p
    JOIN b.bookAuthors ba
    JOIN ba.author a
    JOIN b.genres g
    JOIN b.bookAwards baw
    JOIN baw.award aw
    WHERE (LOWER(b.title) LIKE LOWER(CONCAT('%',:title,'%')) OR :title IS NULL)
      AND (:isbn IS NULL OR b.isbn = :isbn)
      AND (LOWER(p.name) = LOWER(:publisher) OR :publisher IS NULL)
      AND (LOWER(a.name) = LOWER(:authorName) OR :authorName IS NULL)
      AND(:genres IS NULL OR g IN :genres)
      AND(LOWER(aw.name) LIKE LOWER(CONCAT('%',:award,'%')) OR :award IS NULL)
      AND(b.rating = :rating OR :rating IS NULL)
      AND(b.rating > :ratingAbove OR :ratingAbove IS NULL)
""")
    Page<Book> getBooksByCriteria(@Param("title") String title,
                                  @Param("publisher") String publisher,
                                  @Param("genres") Set<Genre> genres,
                                  @Param("isbn") Long isbn,
                                  @Param("authorName") String authorName,
                                  @Param("award") String award,
                                  @Param("rating") Double rating,
                                  @Param("ratingAbove") Double ratingAbove,
                                  Pageable pageable);

    Book getBookByIsbn(Long isbn);

    Book getBookById(Long id);

    List<Book> getBooksByTitle(String title);

    List<Book> getBooksByGenres(Genre genre);

    List<Book> getBooksByGenres(Set<Genre> genres);

    List<Book> getBookByPublisher(Publisher publisher);

    @Query("SELECT b.isbn FROM Book b")
    Set<Long> findAllIsbn();
}
