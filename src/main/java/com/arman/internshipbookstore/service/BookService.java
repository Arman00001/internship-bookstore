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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
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



    public PagedModel<BookDto> searchBooks(BookSearchCriteria bookSearchCriteria, Integer page, Integer size, String sort) {
        String title = bookSearchCriteria.getTitle();
        String publisher = bookSearchCriteria.getPublisher();
        Long isbn = bookSearchCriteria.getIsbn();
        String authorName = bookSearchCriteria.getAuthorName();
        String award = bookSearchCriteria.getAward();
        Double rating = bookSearchCriteria.getRating();
        Double ratingAbove = bookSearchCriteria.getRatingAbove();
        if(rating!=null) ratingAbove=null;

        Set<Genre> genres = null;
        if(bookSearchCriteria.getGenres()!=null) {
            genres = new HashSet<>();
            for (String genre : bookSearchCriteria.getGenres()) {
                genres.add(Genre.fromString(genre));
            }
        }

        Pageable pageable;
        if(sort==null || sort.isBlank())
            pageable = PageRequest.of(page, size);
        else pageable = PageRequest.of(page,size, Sort.by(sort));


        Page<Book> books = bookRepository.getBooksByCriteria(
                title,publisher,genres, isbn,authorName,award,rating,ratingAbove,pageable);

        Page<BookDto> bookDtos = books.map(bookMapper::mapToDto);

        PagedModel<BookDto> pagedModel = new PagedModel<>(bookDtos);

        return pagedModel;
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

    public Book getBookByBookId(Long bookId){
        Book book = bookRepository.getBookById(bookId);

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

    public Set<Long> findAllIsbn(){
        return bookRepository.findAllIsbn();
    }

    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }
}