package com.arman.internshipbookstore.service.dto.review;

import com.arman.internshipbookstore.persistence.entity.BookReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class BookReviewResponseDto {

    private Long id;
    private Long userId;
    private Long bookId;
    private String review;
    private Integer rating;
    private LocalDateTime postedOn;
    private LocalDateTime editedOn;

    public BookReviewResponseDto(Long id, Long userId, Long bookId, String review, Integer rating, LocalDateTime postedOn, LocalDateTime editedOn) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.review = review;
        this.rating = rating;
        this.postedOn = postedOn;
        this.editedOn = editedOn;
    }

    public static BookReviewResponseDto getBookReviewResponse(BookReview bookReview){
        return new BookReviewResponseDto(bookReview.getId(),
                bookReview.getUser().getId(),
                bookReview.getBook().getId(),
                bookReview.getReview(),
                bookReview.getRating(),
                bookReview.getPostedOn(),
                bookReview.getEditedOn());
    }
}
