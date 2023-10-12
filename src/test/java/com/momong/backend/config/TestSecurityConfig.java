package com.momong.backend.config;

import com.momong.backend.global.auth.*;
import com.momong.backend.global.common.properties.JwtProperties;
import com.momong.backend.global.config.SecurityConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
        SecurityConfig.class,
        JwtAccessDeniedHandler.class,
        JwtAuthenticationEntryPoint.class,
        JwtExceptionFilter.class,
        JwtAuthenticationFilter.class,
        JwtTokenProvider.class,
})
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties("default_secret_key_for_test_env_ge_256bit");
    }
}
