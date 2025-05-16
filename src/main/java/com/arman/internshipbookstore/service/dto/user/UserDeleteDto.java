package com.arman.internshipbookstore.service.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDeleteDto {

    @NotBlank
    private String password;

}
