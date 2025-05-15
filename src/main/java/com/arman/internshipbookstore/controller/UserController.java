package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.service.UserService;
import com.arman.internshipbookstore.service.dto.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserDto> updateUserProfile(@PathVariable Long id, @RequestBody @Valid UserProfileUpdateDto userProfileUpdateDto){
        return ResponseEntity.ok(userService.updateUser(id, userProfileUpdateDto));
    }

    @PutMapping("/{id}/email")
    public ResponseEntity<Void> updateUserEmail(@PathVariable("id") Long id,
                                                UserEmailUpdateDto userEmailUpdateDto,
                                                Authentication auth){
        userService.updateUserEmail(id,userEmailUpdateDto,auth);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updateUserPassword(@PathVariable("id") Long id,
                                                   UserPasswordUpdateDto userPasswordUpdateDto,
                                                   Authentication auth){
        userService.updateUserPassword(id,userPasswordUpdateDto,auth);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/role")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> updateUserRole(@PathVariable("id") Long id,
                                               UserRoleUpdateDto userRoleUpdateDto){
        userService.updateUserRole(id,userRoleUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
