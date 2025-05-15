package com.arman.internshipbookstore.service.dto.user;

import com.arman.internshipbookstore.security.annotation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPasswordUpdateDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    @ValidPassword
    private String password;

}