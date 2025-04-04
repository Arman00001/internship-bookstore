package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/getBookByName")
    public Book getBookByName(@RequestParam String title){
        return bookService.getBookByTitle(title);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody Book book){
        bookService.save(book);
    }

}
