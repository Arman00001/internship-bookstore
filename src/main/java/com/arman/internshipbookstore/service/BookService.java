package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.enums.Genre;
import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.dto.BookSearchCriteria;
import com.arman.internshipbookstore.service.dto.PublisherDto;
import com.arman.internshipbookstore.service.exception.BookNotFoundException;
import com.arman.internshipbookstore.service.mapper.BookMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookMapper bookMapper;



    public Page<BookDto> searchBooks(BookSearchCriteria bookSearchCriteria, Integer page, Integer size, String sort) {
//        Specification<Book> specification = Specification.where(null);
//
//        if(bookSearchCriteria.getIsbn()!=null){
//            Long isbn = Long.parseLong(bookSearchCriteria.getIsbn());
//            specification.and(new Specification<Book>() {
//                @Override
//                public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                    return criteriaBuilder.equal(root.get("isbn"),isbn);
//                }
//            });
//        }
//
//        if(bookSearchCriteria.getTitle()!=null){
//            specification.and(new Specification<Book>() {
//                @Override
//                public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                    return criteriaBuilder.equal(root.get("title"), bookSearchCriteria.getTitle());
//                }
//            });
//        }
//
//        if(bookSearchCriteria.getPublisher()!=null){
//            Publisher publisher = publisherService.getPublisherByName(bookSearchCriteria.getPublisher());
//            specification.and(new Specification<Book>() {
//                @Override
//                public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                    return criteriaBuilder.equal(root.get("publisher"), publisher);
//                }
//            });
//        }
//
//        if (bookSearchCriteria.getGenres()!=null){
//            Set<Genre> genres = new HashSet<>();
//            for (String genre : bookSearchCriteria.getGenres()) {
//                genres.add(Genre.fromString(genre));
//            }
//
//            specification.and(new Specification<Book>() {
//                @Override
//                public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                    return criteriaBuilder.equal(root.get("genres"), genres);
//                }
//            });
//        }
//
//        List<Book> books = bookRepository.findAll(specification);
        String title = bookSearchCriteria.getTitle();
        String publisher = bookSearchCriteria.getPublisher();
        Long isbn = null;
        if(bookSearchCriteria.getIsbn()!=null)
                isbn = Long.parseLong(bookSearchCriteria.getIsbn());

        String authorName = bookSearchCriteria.getAuthorName();
        Set<Genre> genres = null;
        if(bookSearchCriteria.getGenres()!=null) {
            genres = new HashSet<>();
            for (String genre : bookSearchCriteria.getGenres()) {
                genres.add(Genre.fromString(genre));
            }
        }

        String award = bookSearchCriteria.getAward();
        Double rating = bookSearchCriteria.getRating();
        Double ratingAbove = bookSearchCriteria.getRatingAbove();
        if(rating!=null) ratingAbove=null;


        Pageable pageable;
        if(sort==null || sort.isBlank())
            pageable = PageRequest.of(page, size);
        else pageable = PageRequest.of(page,size, Sort.by(sort));


        Page<Book> books = bookRepository.getBooksByCriteria(
                title,publisher,genres, isbn,authorName,award,rating,ratingAbove,pageable);
        Page<BookDto> bookDtos = books.map(bookMapper::mapToDto);


        return bookDtos;
    }


    public List<BookDto> getBookByTitle(String title) {
        List<Book> books = bookRepository.getBooksByTitle(title);
        if(books.isEmpty()) throw new BookNotFoundException("There are no books with this title: %s".formatted(title));

        List<BookDto> bookDtos = bookMapper.mapToDtos(books);

        return bookDtos;
    }

    public List<BookDto> getBooksByGenre(String genre_) {
        Genre genre = Genre.fromString(genre_);
        List<Book> books = bookRepository.getBooksByGenres(genre);
        if(books.isEmpty()) throw new BookNotFoundException("There are no books with this genre: %s".formatted(genre_));

        List<BookDto> bookDtos = bookMapper.mapToDtos(books);

        return bookDtos;
    }

    public Book getBookByBookId(String bookId){
        Book book = bookRepository.getBookByBookId(bookId);

        return book;
    }


    public Book save(BookDto bookDto) {
        Publisher publisher = publisherService.getPublisherByName(bookDto.getPublisherName());
        if(publisher==null){
            PublisherDto publisherDto = new PublisherDto();
            publisher = publisherService.save(publisherDto);
        }

        Book book = bookMapper.mapDtoToBook(bookDto);
        book.setPublisher(publisher);

        Book book1 = bookRepository.save(book);

        return book1;
    }

    public Book getBookByIsbn(Long isbn) {
        return bookRepository.getBookByIsbn(isbn);
    }

}