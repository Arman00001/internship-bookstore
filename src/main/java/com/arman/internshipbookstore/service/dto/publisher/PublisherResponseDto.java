package com.arman.internshipbookstore.service.dto.publisher;

import com.arman.internshipbookstore.persistence.entity.Publisher;
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

    public PublisherResponseDto(Publisher publisher) {
        this.id = publisher.getId();
        this.name = publisher.getName();
    }

    public static PublisherResponseDto getPublisherResponse(Publisher publisher) {
        return new PublisherResponseDto(publisher);
    }
}
