package com.arman.internshipbookstore.controller;

import com.arman.internshipbookstore.security.dto.LoginRequestDto;
import com.arman.internshipbookstore.security.dto.LoginResponse;
import com.arman.internshipbookstore.security.util.JwtUtil;
import com.arman.internshipbookstore.service.AuthenticationService;
import com.arman.internshipbookstore.service.UserService;
import com.arman.internshipbookstore.service.dto.user.UserDto;
import com.arman.internshipbookstore.service.dto.user.UserRegistrationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authenticationService.authenticate(loginRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userRegistrationDto));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody String refreshToken) {
        if (refreshToken == null || !jwtUtil.isVerified(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtUtil.getUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        String newAccessToken  = jwtUtil.generateAccessToken(user);

        LoginResponse resp = LoginResponse.builder()
                .withUsername(user.getUsername())
                .withAccessToken(newAccessToken)
                .withRefreshToken(refreshToken)
                .build();

        return ResponseEntity.ok(resp);
    }
}

