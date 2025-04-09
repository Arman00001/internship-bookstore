package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.dto.PublisherDto;
import com.arman.internshipbookstore.service.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherService publisherService;
    private final AuthorService authorService;
    private final AwardService awardService;
    private final BookMapper bookMapper;


    public BookDto getBookByTitle(String title) {
        Book book = bookRepository.getBookByTitle(title);

        return bookMapper.mapToDto(book);
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