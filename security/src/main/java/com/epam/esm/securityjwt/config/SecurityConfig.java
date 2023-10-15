package com.epam.esm.securityjwt.config;

import com.epam.esm.TokenRepository;
import com.epam.esm.model.enums.UserRole;
import com.epam.esm.securityjwt.JwtService;
import com.epam.esm.securityjwt.filter.AuthTokenFilter;
import com.epam.esm.securityjwt.jwt.AuthEntryPointJwt;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@Data
public class SecurityConfig {

    private static final String CERTIFICATES_PATH = "/certificates/**";
    private static final String TAGS_PATH = "/tags/**";
    private static final String RECEIPTS_PATH = "/receipts/**";

    private final UserDetailsService userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;

    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           AuthTokenFilter authTokenFilter,
                                           AuthenticationProvider authenticationProvider) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(POST, "/auth/**")
                                .permitAll()
                                .requestMatchers(GET, CERTIFICATES_PATH)
                                .permitAll()
                                .requestMatchers(POST, "/certificates/find-by-tags")
                                .permitAll()
                                .requestMatchers(POST, RECEIPTS_PATH)
                                .hasAuthority(UserRole.CUSTOMER.getRoleName())
                                .requestMatchers(GET, RECEIPTS_PATH)
                                .hasAnyAuthority(UserRole.ADMIN.getRoleName(), UserRole.CUSTOMER.getRoleName())
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
                )
                .logout(logout ->
                        logout
                                .logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()));

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(authenticationProvider);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Додайте адресу вашого фронтенду
        configuration.setAllowedMethods(List.of("*")); // Додайте необхідні HTTP-методи
        configuration.setAllowedHeaders(List.of("*")); // Дозвольте всі заголовки
        configuration.setAllowCredentials(true); // Дозволити передачу креденцій (cookies, авторизація)
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter(UserDetailsService userDetailsService,
                                                        JwtService jwtService,
                                                        TokenRepository tokenRepository) {
        return new AuthTokenFilter(jwtService, userDetailsService, tokenRepository);
    }


    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}