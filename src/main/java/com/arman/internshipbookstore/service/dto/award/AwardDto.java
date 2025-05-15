package com.arman.internshipbookstore.service.dto.award;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
public class AwardDto {

    private Long id;

    private String name;

}
