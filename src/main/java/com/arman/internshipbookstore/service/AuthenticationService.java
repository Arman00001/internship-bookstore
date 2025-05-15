package com.arman.internshipbookstore.service;

import com.arman.internshipbookstore.security.dto.LoginRequestDto;
import com.arman.internshipbookstore.security.dto.LoginResponse;
import com.arman.internshipbookstore.security.dto.RefreshTokenDto;
import com.arman.internshipbookstore.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public LoginResponse authenticate(LoginRequestDto loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtil.generateAccessToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return LoginResponse.builder()
                .withUsername(userDetails.getUsername())
                .withAccessToken(accessToken)
                .withRefreshToken(refreshToken)
                .build();
    }

    public Optional<LoginResponse> refreshToken(RefreshTokenDto refreshTokenDto) {

        String refreshToken = refreshTokenDto.getRefreshToken();

        if (refreshToken == null || !jwtUtil.isVerified(refreshToken)) {
            return Optional.empty();
        }

        String username = jwtUtil.getUsername(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);

        String newAccessToken = jwtUtil.generateAccessToken(user);

        LoginResponse resp = LoginResponse.builder()
                .withUsername(user.getUsername())
                .withAccessToken(newAccessToken)
                .withRefreshToken(refreshToken)
                .build();

        return Optional.of(resp);
    }
}
