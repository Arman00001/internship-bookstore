package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.dto.BookDto;
import com.arman.internshipbookstore.service.dto.BookSearchCriteria;
import com.arman.internshipbookstore.service.dto.BookUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/get")
    public PagedModel<BookDto> getBooks(@ModelAttribute BookSearchCriteria bookSearchCriteria,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("size") Integer size,
                                        @RequestParam(required = false, name = "sort") String sort) {
        return bookService.searchBooks(bookSearchCriteria, page, size, sort);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public BookDto addBook(@RequestBody @Valid BookDto bookDto) {
        return bookService.addBook(bookDto);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public BookDto updateBook(@RequestBody @Valid BookUpdateDto bookUpdateDto) {
        return bookService.updateBook(bookUpdateDto);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBook(@RequestParam("id") Long id) {
        bookService.deleteBook(id);
    }


}
