package com.edusys.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**", "/test", "/error").permitAll()
                .requestMatchers("/api/v1/admins/**", "/api/v1/users/**", "/api/v1/fee-records/**", "/api/v1/receipts/**", "/api/v1/inquiries/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/teachers/**", "/api/v1/courses/**", "/api/v1/batches/**", "/api/v1/semesters/**", "/api/v1/question-bank/**", "/api/v1/exams/**", "/api/v1/assignments/**", "/api/v1/grades/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/api/v1/reviewers/**", "/api/v1/career-tasks/**", "/api/v1/evaluations/**", "/api/v1/career-levels/**").hasAnyRole("ADMIN", "REVIEWER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
