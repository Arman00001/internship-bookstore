package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @GetMapping("/getBookByName")
    public Book getBookByName(@RequestParam String title){
        return bookRepository.getBookByTitle(title);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody Book book){
        bookRepository.save(book);
    }

}
