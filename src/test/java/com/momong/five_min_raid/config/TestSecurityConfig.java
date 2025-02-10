package com.momong.five_min_raid.config;

import com.momong.five_min_raid.global.auth.*;
import com.momong.five_min_raid.global.common.properties.FMRaidProperties;
import com.momong.five_min_raid.global.common.properties.JwtProperties;
import com.momong.five_min_raid.global.config.SecurityConfig;
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

    @Bean
    public FMRaidProperties fmRaidProperties() {
        return new FMRaidProperties(-1L, "https://fmraid.test");
    }
}
