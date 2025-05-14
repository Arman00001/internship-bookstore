package com.arman.internshipbookstore.service.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class PublisherDto {
    private Long id;

    @NotBlank(message = "Publisher name should not be empty")
    private String name;
}
