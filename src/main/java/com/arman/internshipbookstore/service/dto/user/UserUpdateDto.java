package com.arman.internshipbookstore.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;
}
