package com.vueones.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 已禁用登录认证功能
 */
@Configuration
@EnableWebSecurity
@ConditionalOnProperty(prefix = "security", name = "enabled", havingValue = "false")
public class SecurityConfig {

    /**
     * 配置安全过滤器链
     * 所有请求均允许访问，不需要身份验证
     * @param http HTTP安全配置
     * @return 过滤器链
     * @throws Exception 可能的异常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // .requestMatchers("/user/**", "/", "/css/**", "/js/**", "/images/**").permitAll()
            // .requestMatchers("/api/**").authenticated()
            // 允许所有请求通过，不需要认证
            .anyRequest().permitAll()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
    
    /**
     * 密码编码器
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
} 