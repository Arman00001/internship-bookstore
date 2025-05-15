package com.arman.internshipbookstore.service.dto.award;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardCreateDto {

    @NotBlank(message = "Award name should not be empty")
    private String name;

}
