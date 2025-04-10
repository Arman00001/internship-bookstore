package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.dto.BookSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/searchBook")
    public List<BookDto> getBooks(@ModelAttribute BookSearchCriteria bookSearchCriteria) {
        return bookService.searchBooks(bookSearchCriteria);
    }

    @GetMapping("/getBooksByTitle")
    public List<BookDto> getBookByTitle(@RequestParam("title") String title){
        return bookService.getBookByTitle(title);
    }

    @GetMapping("/getBooksByGenre")
    public List<BookDto> getBooksByGenre(@RequestParam("genre") String genre){
        return bookService.getBooksByGenre(genre);
    }

    @PostMapping("/addBook")
    public void addBook(@RequestBody BookDto bookDto){
        bookService.save(bookDto);
    }

}
