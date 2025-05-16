package com.arman.internshipbookstore.service.criteria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class AuthorSearchCriteria extends SearchCriteria{

    @NotBlank
    private String name;

    @NotNull
    private String sortAscDesc;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        String sortingParam = "name";
        Sort.Direction direction = sortAscDesc.isBlank() ? Sort.Direction.ASC : Sort.Direction.fromString(sortAscDesc);

        return pageRequest.withSort(
                Sort.by(direction, sortingParam)
        );
    }
}
