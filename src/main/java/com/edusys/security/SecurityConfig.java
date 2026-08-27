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
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource;

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
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/v1/auth/**", "/test/**", "/error", "/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/course-access-grants/**").hasAnyRole("ADMIN", "STUDENT")
                .requestMatchers("/api/v1/course-access-grants/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/v1/fee-records/**", "/api/v1/receipts/**").hasAnyRole("ADMIN", "STUDENT", "PARENT")
                .requestMatchers(HttpMethod.GET, "/api/v1/career-tasks/**").hasAnyRole("ADMIN", "TEACHER", "REVIEWER", "STUDENT")
                .requestMatchers(HttpMethod.GET, "/api/v1/courses/**", "/api/v1/batches/**", "/api/v1/semesters/**", "/api/v1/assignments/**", "/api/v1/exams/**", "/api/v1/grades/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT", "PARENT")
                .requestMatchers(HttpMethod.GET, "/api/v1/career-levels/**").hasAnyRole("ADMIN", "REVIEWER", "STUDENT")
                .requestMatchers("/api/v1/career/tasks/*/submissions", "/api/v1/career/submissions/mine", "/api/v1/career/progress").hasRole("STUDENT")
                .requestMatchers("/api/v1/career/submissions/**").hasAnyRole("ADMIN", "REVIEWER")
                .requestMatchers("/api/v1/career/students/*/override").hasRole("ADMIN")
                .requestMatchers("/api/v1/admins/**", "/api/v1/users/**", "/api/v1/fee-records/**", "/api/v1/receipts/**", "/api/v1/inquiries/**", "/api/v1/dashboard/**", "/api/v1/reports/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/exams/*/questions", "/api/v1/exams/*/submit").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .requestMatchers("/api/v1/teachers/**", "/api/v1/courses/**", "/api/v1/batches/**", "/api/v1/semesters/**", "/api/v1/question-bank/**", "/api/v1/exams/**", "/api/v1/assignments/**", "/api/v1/grades/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/api/v1/reviewers/**", "/api/v1/career-tasks/**", "/api/v1/career-levels/**").hasAnyRole("ADMIN", "REVIEWER")
                .requestMatchers("/api/v1/parent/**").hasAnyRole("ADMIN", "PARENT")
                .requestMatchers("/api/v1/parents/**", "/api/v1/parent-student-links/**", "/api/v1/enrollments/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/students/**").hasAnyRole("ADMIN", "TEACHER")
                .requestMatchers("/api/v1/student-exams/**", "/api/v1/exam-attempts/**", "/api/v1/assignment-submissions/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT")
                .requestMatchers("/api/v1/academic-calendars/**").hasAnyRole("ADMIN", "TEACHER", "STUDENT", "PARENT")
                .requestMatchers("/api/v1/questions/**").hasAnyRole("ADMIN", "TEACHER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
