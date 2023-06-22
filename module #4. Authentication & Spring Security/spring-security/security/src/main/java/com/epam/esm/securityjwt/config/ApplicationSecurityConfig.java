package com.epam.esm.securityjwt.config;

import com.epam.esm.model.enums.UserRole;
import com.epam.esm.securityjwt.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;


@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig {

    private static final String CERTIFICATES_PATH = "/certificates/**";
    private static final String TAGS_PATH = "/tags/**";
    private static final String RECEIPTS_PATH = "/receipts/**";

    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthenticationFilter authenticationFilter,
                                           AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(POST, "/auth/**")
                                .permitAll()
                                .requestMatchers(GET, CERTIFICATES_PATH)
                                .permitAll()
                                .requestMatchers(POST, "/certificates/find-by-tags")
                                .permitAll()
                                .requestMatchers(POST, RECEIPTS_PATH)
                                .hasAuthority(UserRole.USER.getRoleName())
                                .requestMatchers(GET, RECEIPTS_PATH)
                                .hasAnyAuthority(UserRole.ADMIN.getRoleName(), UserRole.USER.getRoleName())
                                .requestMatchers(POST, "/tags/create", "/certificates/create")
                                .hasAuthority(UserRole.ADMIN.getRoleName())
                                .requestMatchers(GET, "/users/**", TAGS_PATH)
                                .hasAuthority(UserRole.ADMIN.getRoleName())
                                .requestMatchers(DELETE, TAGS_PATH, CERTIFICATES_PATH, RECEIPTS_PATH)
                                .hasAuthority(UserRole.ADMIN.getRoleName())
                                .requestMatchers(PATCH, CERTIFICATES_PATH)
                                .hasAuthority(UserRole.ADMIN.getRoleName())
                                .anyRequest()
                                .authenticated()
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider);
        return http.build();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
