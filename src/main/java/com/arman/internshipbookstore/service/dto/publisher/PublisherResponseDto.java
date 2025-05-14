package com.arman.internshipbookstore.service.dto.publisher;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PublisherResponseDto {
    private Long id;

    private String name;

    public PublisherResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
