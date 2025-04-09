package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/getBookByName")
    public BookDto getBookByName(@RequestParam String title){
        return bookService.getBookByTitle(title);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody BookDto bookDto){
        bookService.save(bookDto);
    }

}
