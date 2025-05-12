package com.arman.internshipbookstore.security;

import com.arman.internshipbookstore.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(this.passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);

        return authenticationProvider;
    }

    @Bean
    public AuthorizationFilter authorizationFilter() {
        return new AuthorizationFilter(jwtUtil);
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity httpSecurity,
                                         CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                         AccessDeniedHandler accessDeniedHandler) throws Exception {
        return httpSecurity.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/api/auth/**").permitAll()

                                .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**",
                                        "/api/publishers/**", "/api/awards/**").permitAll()

                                .requestMatchers(HttpMethod.POST, "/api/reviews/**").hasRole("USER")

                                .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/authors/**",
                                        "/api/publishers/**", "/api/awards/**", "/api/csv/**").hasAnyRole("MODERATOR", "ADMIN")

                                .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/authors/**",
                                        "/api/publishers/**", "/api/awards/**").hasAnyRole("MODERATOR", "ADMIN")

                                .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/authors/**",
                                        "/api/publishers/**", "/api/awards/**",
                                        "/api/reviews/**").hasAnyRole("MODERATOR", "ADMIN")

                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(e->
                                e.authenticationEntryPoint(customAuthenticationEntryPoint)
                                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authenticationProvider(this.authenticationProvider())
                .addFilterBefore(this.authorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
