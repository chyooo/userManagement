package com.usermanagement.config;

import com.usermanagement.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 웹 보안 기능 활성화
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService; // 사용자 상세 정보를 제공하는 서비스

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/signup").permitAll() // 로그인 및 회원가입은 모든 사용자 접근 허용
                .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
            )
            .formLogin(form -> form
                .loginPage("/login") // 로그인 페이지 URL
                .defaultSuccessUrl("/main") // 로그인 성공 후 리다이렉트할 URL
                .failureUrl("/login?error=true") // 로그인 실패 시 리다이렉트할 URL
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // 로그아웃 URL 설정
                .logoutSuccessUrl("/login?logout=true") // 로그아웃 성공 후 리다이렉트할 URL
                .permitAll() // 모든 사용자 접근 허용
            );

        return http.build(); // SecurityFilterChain 반환
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화에 사용할 BCryptPasswordEncoder 반환
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService(customUserDetailsService) // 사용자 상세 정보 서비스를 설정
            .passwordEncoder(passwordEncoder()); // 비밀번호 인코더 설정

        return authenticationManagerBuilder.build(); // AuthenticationManager 반환
    }
}
