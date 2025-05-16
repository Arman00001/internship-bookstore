package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.security.dto.LoginRequestDto;
import com.arman.internshipbookstore.security.dto.LoginResponse;
import com.arman.internshipbookstore.service.AuthenticationService;
import com.arman.internshipbookstore.service.UserService;
import com.arman.internshipbookstore.service.dto.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long id,
                                                     @RequestBody @Valid UserProfileUpdateDto userProfileUpdateDto){
        return ResponseEntity.ok(userService.updateUserProfile(id, userProfileUpdateDto));
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<LoginResponse> updateUserEmail(@PathVariable("id") Long id,
                                                         @RequestBody @Valid UserEmailUpdateDto userEmailUpdateDto,
                                                         Authentication auth){

        LoginRequestDto loginRequestDto = userService.updateUserEmail(id,userEmailUpdateDto,auth);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationService.authenticate(loginRequestDto));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<LoginResponse> updateUserPassword(@PathVariable("id") Long id,
                                                   @RequestBody @Valid UserPasswordUpdateDto userPasswordUpdateDto,
                                                   Authentication auth){
        LoginRequestDto loginRequestDto = userService.updateUserPassword(id,userPasswordUpdateDto,auth);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(authenticationService.authenticate(loginRequestDto));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<UserDto> updateUserRole(@PathVariable("id") Long id,
                                               @RequestBody @Valid UserRoleUpdateDto userRoleUpdateDto){
        return userService.updateUserRole(id,userRoleUpdateDto).map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
