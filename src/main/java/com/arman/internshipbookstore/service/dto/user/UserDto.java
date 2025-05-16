package com.arman.internshipbookstore.service.dto.user;

import com.arman.internshipbookstore.persistence.entity.UserProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Long createdAt;
    private Long updatedAt;
    private String role;

    public static UserDto toDto(UserProfile userProfile){
        UserDto userDto = new UserDto();

        userDto.setId(userProfile.getId());
        userDto.setFirstname(userProfile.getFirstName());
        userDto.setLastname(userProfile.getLastName());
        userDto.setEmail(userProfile.getEmail());
        userDto.setCreatedAt(userProfile.getCreatedAt().getEpochSecond());
        userDto.setUpdatedAt(userProfile.getUpdatedAt().getEpochSecond());
        userDto.setRole(userProfile.getRole().getName().toString());

        return userDto;
    }

}
