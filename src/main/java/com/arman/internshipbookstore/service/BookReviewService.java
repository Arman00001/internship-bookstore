package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.entity.Book;
import com.arman.internshipbookstore.persistence.entity.BookReview;
import com.arman.internshipbookstore.persistence.entity.UserProfile;
import com.arman.internshipbookstore.persistence.repository.BookRepository;
import com.arman.internshipbookstore.persistence.repository.BookReviewRepository;
import com.arman.internshipbookstore.persistence.repository.UserProfileRepository;
import com.arman.internshipbookstore.service.dto.review.BookReviewCreateDto;
import com.arman.internshipbookstore.service.dto.review.BookReviewResponseDto;
import com.arman.internshipbookstore.service.dto.review.BookReviewUpdateDto;
import com.arman.internshipbookstore.service.exception.BookNotFoundException;
import com.arman.internshipbookstore.service.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final UserProfileRepository userProfileRepository;
    private final BookRepository bookRepository;


    public BookReviewResponseDto addReview(@Valid BookReviewCreateDto bookReviewCreateDto, Authentication auth) {
        String email = auth.getName();

        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookReviewCreateDto.getBookId())
                .orElseThrow(()-> new BookNotFoundException("Book not found")
        );

        BookReview bookReview = new BookReview();
        bookReview.setBook(book);
        bookReview.setUser(user);
        bookReview.setReview(bookReviewCreateDto.getReview());
        bookReview.setRating(bookReviewCreateDto.getRating());

        BookReview review = bookReviewRepository.save(bookReview);

        return BookReviewResponseDto.getBookReviewResponse(review);
    }

    public BookReviewResponseDto editReview(Long reviewId, @Valid BookReviewUpdateDto bookReviewUpdateDto, Authentication auth) {
        String email = auth.getName();

        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookReviewUpdateDto.getBookId())
                .orElseThrow(()-> new BookNotFoundException("Book not found")
                );
        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(()->new ResourceNotFoundException("Review not found"));
        bookReview.setRating(bookReviewUpdateDto.getRating());
        bookReview.setReview(bookReviewUpdateDto.getReview());
        bookReview.setEditedOn(LocalDateTime.now());

        BookReview review = bookReviewRepository.save(bookReview);

        return BookReviewResponseDto.getBookReviewResponse(review);
    }

    public void deleteReview(Long bookId, Long reviewId, Authentication auth) {
        String email = auth.getName();

        UserProfile user = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new BookNotFoundException("Book not found")
                );
        BookReview bookReview = bookReviewRepository.findById(reviewId)
                .orElseThrow(()->new ResourceNotFoundException("Review not found"));

        bookReviewRepository.delete(bookReview);
    }
}
