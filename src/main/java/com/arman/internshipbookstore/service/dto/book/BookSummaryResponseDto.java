package com.arman.internshipbookstore.service.dto.book;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookSummaryResponseDto {
    private Long id;
    private String title;
    private String imageUrl;
    private List<String> authorNames;

    public BookSummaryResponseDto(Long id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
