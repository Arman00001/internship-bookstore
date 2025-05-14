package com.arman.internshipbookstore.service.dto.award;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class AwardDto {

    private Long id;

    @NotBlank(message = "Award name should not be empty")
    private String name;

}
