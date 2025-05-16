package com.arman.internshipbookstore.service.dto.user;

import com.arman.internshipbookstore.enums.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleUpdateDto {

    @NotNull
    private RoleName roleName;
}
