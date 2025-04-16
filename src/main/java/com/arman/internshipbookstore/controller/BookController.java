package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.dto.BookSearchCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/searchBook")
    public PagedModel<BookDto> getBooks(@ModelAttribute BookSearchCriteria bookSearchCriteria,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @RequestParam(required = false, name = "sort") String sort) {
        return bookService.searchBooks(bookSearchCriteria, page, size, sort);
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
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@RequestBody BookDto bookDto){
        bookService.save(bookDto);
    }

}
