package com.arman.internshipbookstore.service.criteria;

import com.arman.internshipbookstore.enums.Genre;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Set;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria {

    private String title;
    private String authorName;
    private String publisher;
    private Long isbn;
    private Set<Genre> genres;
    private String award;
    private Double ratingAbove;
    private Double rating;
    private String sort;
    private String sortAscDesc;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        String sortingParam = sort.isBlank() ? "title" : sort;
        Sort.Direction direction = sortAscDesc.isBlank() ? Sort.Direction.ASC : Sort.Direction.fromString(sortAscDesc);

        return pageRequest.withSort(
                Sort.by(direction, sortingParam)
        );
    }
}
