package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.BookReviewService;
import com.arman.internshipbookstore.service.dto.review.BookReviewCreateDto;
import com.arman.internshipbookstore.service.dto.review.BookReviewResponseDto;
import com.arman.internshipbookstore.service.dto.review.BookReviewUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class BookReviewController {

    private final BookReviewService bookReviewService;

    @PostMapping("/review")
    public ResponseEntity<BookReviewResponseDto> addReview(@RequestBody @Valid BookReviewCreateDto bookReviewCreateDto,
                                           Authentication authentication){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookReviewService.addReview(bookReviewCreateDto,authentication));
    }

    @PostMapping("/{reviewId}")
    public ResponseEntity<BookReviewResponseDto> editReview(@PathVariable("reviewId") Long reviewId,
                                            @RequestBody @Valid BookReviewUpdateDto bookReviewUpdateDto,
                                            Authentication authentication){
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookReviewService.editReview(reviewId,bookReviewUpdateDto,authentication));
    }

    @DeleteMapping("/{bookId}/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable("bookId") Long bookId,
                                             @PathVariable("reviewId") Long reviewId,
                                             Authentication authentication){
        bookReviewService.deleteReview(bookId,reviewId,authentication);
        return ResponseEntity.noContent().build();
    }
}
