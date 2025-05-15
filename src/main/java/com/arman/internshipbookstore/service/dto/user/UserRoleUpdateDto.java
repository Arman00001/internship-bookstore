package com.arman.internshipbookstore.service.dto.user;

import com.arman.internshipbookstore.enums.RoleName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleUpdateDto {

    private RoleName roleName;
}
