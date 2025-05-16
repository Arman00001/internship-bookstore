package com.arman.internshipbookstore.security;

import com.arman.internshipbookstore.persistence.entity.Role;
import com.arman.internshipbookstore.persistence.entity.UserCredentials;
import com.arman.internshipbookstore.persistence.entity.UserProfile;
import com.arman.internshipbookstore.persistence.repository.UserCredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials userCredentials = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));

        UserProfile userProfile = userCredentials.getUserProfile();

        if (!userProfile.isEnabled()) {
            throw new LockedException("User is locked");
        }

        final Role role = userProfile.getRole();


        return new org.springframework.security.core.userdetails.User(
                userCredentials.getUsername(),
                userCredentials.getPassword(),
                List.of(new SimpleGrantedAuthority(role.getName().name()))
        );
    }
}
