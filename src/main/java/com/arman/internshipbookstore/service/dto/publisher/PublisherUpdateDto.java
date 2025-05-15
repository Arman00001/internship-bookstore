package com.arman.internshipbookstore.service.dto.publisher;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherUpdateDto {

    @NotBlank
    private String name;

}
