package com.arman.internshipbookstore.service.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherCreateDto {

    @NotBlank(message = "Publisher name should not be empty")
    private String name;

}
