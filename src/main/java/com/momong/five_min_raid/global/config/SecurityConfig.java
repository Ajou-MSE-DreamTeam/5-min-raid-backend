package com.momong.five_min_raid.global.config;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.global.auth.JwtAccessDeniedHandler;
import com.momong.five_min_raid.global.auth.JwtAuthenticationEntryPoint;
import com.momong.five_min_raid.global.auth.JwtAuthenticationFilter;
import com.momong.five_min_raid.global.auth.JwtExceptionFilter;
import com.momong.five_min_raid.global.common.properties.FMRaidProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final Environment env;
    private final FMRaidProperties fmRaidProperties;

    private static final String[] AUTH_WHITE_PATHS = {
            "/favicon.ico",
            "/actuator/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v*/auth/login/**",
            "/api/v*/auth/tokens/refresh",
            "/api/v*/auth/refresh-token/validity"
    };

    private static final Map<String, HttpMethod> AUTH_WHITE_LIST = Map.of(
            "/actuator/health", HttpMethod.GET,
            "/api/v*/client-versions/latest", HttpMethod.GET
    );

    private static final Map<String, HttpMethod> ADMIN_AUTH_LIST = Map.of(
            "/api/v*/notices", HttpMethod.POST,
            "/api/v*/client-versions/latest", HttpMethod.PUT
    );

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(CsrfConfigurer::disable)
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsConfigurer ->
                        corsConfigurer.configurationSource(corsConfigurationSource(List.of(fmRaidProperties.serverUrl())))
                ).authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();
                    auth.requestMatchers(AUTH_WHITE_PATHS).permitAll();
                    AUTH_WHITE_LIST.forEach((path, httpMethod) -> auth.requestMatchers(httpMethod, path).permitAll());
                    ADMIN_AUTH_LIST.forEach((path, httpMethod) -> auth.requestMatchers(httpMethod, path).hasAnyRole(RoleType.ADMIN.name()));
                    auth.anyRequest().authenticated();
                })
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())
                .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource(List<String> allowedOrigins) {
        CorsConfiguration corsConfig = new CorsConfiguration();

        if (Arrays.asList(env.getActiveProfiles()).contains("prod")) {
            corsConfig.setAllowCredentials(true);
            corsConfig.setAllowedOrigins(allowedOrigins);
        } else {
            corsConfig.setAllowCredentials(false);
            corsConfig.setAllowedOrigins(List.of("*"));
        }

        corsConfig.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name()
        ));

        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setExposedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}
