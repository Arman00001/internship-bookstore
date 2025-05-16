package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.BookService;
import com.arman.internshipbookstore.service.dto.PageResponseDto;
import com.arman.internshipbookstore.service.dto.book.*;
import com.arman.internshipbookstore.service.criteria.BookSearchCriteria;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public PageResponseDto<BookSummaryResponseDto> getBooks(@ModelAttribute BookSearchCriteria bookSearchCriteria) {
        return bookService.searchBooks(bookSearchCriteria);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponseDto> addBook(@RequestBody @Valid BookCreateDto bookCreateDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.addBook(bookCreateDto));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<BookResponseDto> updateBook(@RequestBody @Valid BookUpdateDto bookUpdateDto) {
        return ResponseEntity.ok(bookService.updateBook(bookUpdateDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
    }

}
