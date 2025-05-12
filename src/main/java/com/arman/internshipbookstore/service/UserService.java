package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.enums.RoleName;
import com.arman.internshipbookstore.persistence.entity.Role;
import com.arman.internshipbookstore.persistence.entity.UserCredentials;
import com.arman.internshipbookstore.persistence.entity.UserProfile;
import com.arman.internshipbookstore.persistence.repository.RoleRepository;
import com.arman.internshipbookstore.persistence.repository.UserCredentialRepository;
import com.arman.internshipbookstore.persistence.repository.UserProfileRepository;
import com.arman.internshipbookstore.service.dto.user.UserDto;
import com.arman.internshipbookstore.service.dto.user.UserRegistrationDto;
import com.arman.internshipbookstore.service.dto.user.UserUpdateDto;
import com.arman.internshipbookstore.service.exception.ResourceAlreadyUsedException;
import com.arman.internshipbookstore.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserProfileRepository userProfileRepository;
    private final UserCredentialRepository userCredentialRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto createUser(UserRegistrationDto userRegistrationDto) {

        if (userProfileRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new ResourceAlreadyUsedException("User with this email already exists");
        }

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        UserProfile userProfile = new UserProfile();

        userProfile.setFirstName(userRegistrationDto.getFirstName());
        userProfile.setLastName(userRegistrationDto.getLastName());
        userProfile.setEmail(userRegistrationDto.getEmail());
        userProfile.setEnabled(true);
        userProfile.setRole(role);

        UserProfile profile = userProfileRepository.save(userProfile);


        UserCredentials userCredentials = new UserCredentials();

        userCredentials.setUserProfile(profile);
        userCredentials.setUsername(userRegistrationDto.getEmail());
        userCredentials.setPassword(
                passwordEncoder.encode(userRegistrationDto.getPassword())
        );
        userCredentialRepository.save(userCredentials);

        return UserDto.toDto(profile);
    }

    public List<UserDto> getAllUsers(){
        return userProfileRepository.findAll().stream().map(UserDto::toDto).toList();
    }

    public UserDto getById(Long id){
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        return UserDto.toDto(userProfile);
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto userUpdateDto){
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        userProfile.setFirstName(userUpdateDto.getFirstname());
        userProfile.setLastName(userUpdateDto.getLastname());

        return UserDto.toDto(userProfileRepository.save(userProfile));
    }

    public void deleteUser(Long id){
        UserCredentials credentials = userCredentialRepository.findByUserProfileId(id)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        userCredentialRepository.delete(credentials);
    }
}
