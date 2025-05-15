package com.arman.internshipbookstore.service.dto.award;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwardUpdateDto {

    @NotBlank
    private String name;

}
