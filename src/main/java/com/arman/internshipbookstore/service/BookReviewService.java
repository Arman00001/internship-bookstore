package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.persistence.repository.BookReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewService {
    private final BookReviewRepository bookReviewRepository;


}
