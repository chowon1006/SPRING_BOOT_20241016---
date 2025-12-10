package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration        // 스프링 설정 클래스
@EnableWebSecurity    // 스프링 시큐리티 활성화
public class SecurityConfig {

    // 보안 필터 체인 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // XSS 방어 헤더 설정
            .headers(headers -> headers
                .addHeaderWriter((request, response) -> {
                    response.setHeader("X-XSS-Protection", "1; mode=block");
                })
            )

            // CSRF 기본 설정 (폼 전송 사용 시 유지)
            // .csrf(withDefaults())
            .csrf(csrf -> csrf.disable())

            // 세션 관리
            .sessionManagement(session -> session
                .invalidSessionUrl("/session-expired")   // 세션 만료 시 이동 페이지
                .maximumSessions(1)                      // 사용자당 최대 세션 수
                .maxSessionsPreventsLogin(true)          // 동시 로그인 방지
            );

        return http.build(); // 필터 체인 반환
    }

    // 비밀번호 암호화용 Bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}