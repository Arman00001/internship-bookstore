package com.arman.internshipbookstore.service.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class AwardSearchCriteria extends SearchCriteria{

    private String name;

    private String sortAscDesc;
    private String sort;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        String sortingParam = sort == null || sort.isBlank() ? "name" : sort;
        Sort.Direction direction = sortAscDesc==null || sortAscDesc.isBlank() ? Sort.Direction.ASC : Sort.Direction.fromString(sortAscDesc);

        return pageRequest.withSort(
                Sort.by(direction, sortingParam)
        );
    }
}
