package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.Publisher;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherService publisherService;


    public Book getBookByTitle(String title) {
        return bookRepository.getBookByTitle(title);
    }


    public void save(Book book) {
        Publisher publisher;
        if(publisherService.getPublisherByName(book.getPublisher().getName())==null){
             publisher = publisherService.save(book.getPublisher());
        } else publisher = publisherService.getPublisherByName(book.getPublisher().getName());

        book.setPublisher(publisher);
        bookRepository.save(book);

    }
}