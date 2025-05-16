package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.enums.RoleName;
import com.arman.internshipbookstore.persistence.entity.Role;
import com.arman.internshipbookstore.persistence.entity.UserCredentials;
import com.arman.internshipbookstore.persistence.entity.UserProfile;
import com.arman.internshipbookstore.persistence.repository.RoleRepository;
import com.arman.internshipbookstore.persistence.repository.UserCredentialRepository;
import com.arman.internshipbookstore.persistence.repository.UserProfileRepository;
import com.arman.internshipbookstore.security.dto.LoginRequestDto;
import com.arman.internshipbookstore.service.dto.user.*;
import com.arman.internshipbookstore.service.exception.ResourceAlreadyUsedException;
import com.arman.internshipbookstore.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        UserCredentials userCredentials = new UserCredentials();

        userCredentials.setUserProfile(userProfile);
        userCredentials.setUsername(userRegistrationDto.getEmail());
        userCredentials.setPassword(
                passwordEncoder.encode(userRegistrationDto.getPassword())
        );

        userProfile.setCredentials(userCredentials);


        return UserDto.toDto(userProfileRepository.save(userProfile));
    }

    public List<UserDto> getAllUsers() {
        return userProfileRepository.findAll().stream().map(UserDto::toDto).toList();
    }

    public UserDto getById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserDto.toDto(userProfile);
    }

    @Transactional
    public UserDto updateUserProfile(Long id, UserProfileUpdateDto userProfileUpdateDto) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userProfile.setFirstName(userProfileUpdateDto.getFirstname());
        userProfile.setLastName(userProfileUpdateDto.getLastname());

        return UserDto.toDto(userProfileRepository.save(userProfile));
    }

    public void deleteUser(Long id) {
        UserCredentials credentials = userCredentialRepository.findByUserProfileId(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userCredentialRepository.delete(credentials);
    }

    @Transactional
    public Optional<UserDto> updateUserRole(Long id, UserRoleUpdateDto userRoleUpdateDto) {
        UserProfile user = userProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Role role = roleRepository.findByName(userRoleUpdateDto.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.setRole(role);

        return Optional.of(UserDto.toDto(userProfileRepository.save(user)));
    }

    public LoginRequestDto updateUserPassword(Long id, UserPasswordUpdateDto userPasswordUpdateDto, Authentication auth) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (checkUserAuthentication(userProfile, auth, userPasswordUpdateDto.getCurrentPassword())) {
            UserCredentials userCredentials = userProfile.getCredentials();

            userCredentials.setPassword(passwordEncoder.encode(userPasswordUpdateDto.getPassword()));

            userCredentialRepository.save(userCredentials);

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUsername(auth.getName());
            loginRequestDto.setPassword(userPasswordUpdateDto.getPassword());

            return loginRequestDto;
        }
        throw new IllegalStateException("User not authenticated to perform such action");
    }

    @Transactional
    public LoginRequestDto updateUserEmail(Long id, UserEmailUpdateDto userEmailUpdateDto, Authentication auth) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        if (checkUserAuthentication(userProfile, auth, userEmailUpdateDto.getCurrentPassword())) {

            userProfile.getCredentials().setUsername(userEmailUpdateDto.getEmail());
            userProfile.setEmail(userEmailUpdateDto.getEmail());

            userProfileRepository.save(userProfile);

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUsername(userEmailUpdateDto.getEmail());
            loginRequestDto.setPassword(userEmailUpdateDto.getCurrentPassword());

            return loginRequestDto;
        }
        throw new IllegalStateException("User not authenticated to perform such action");
    }

    private boolean checkUserAuthentication(UserProfile userProfile, Authentication authentication, String password) {
        return userProfile.getEmail().equals(authentication.getName())
                && passwordEncoder.matches(password,userProfile.getCredentials().getPassword());
    }
}
