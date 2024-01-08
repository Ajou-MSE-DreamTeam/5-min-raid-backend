package com.momong.five_min_raid.global.auth;

import com.momong.five_min_raid.domain.member.service.MemberQueryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class CustomUserDetailsService {

    @Bean
    public UserDetailsService userDetailsService(MemberQueryService memberQueryService) {
        return username -> new UserPrincipal(memberQueryService.getDtoById(Long.parseLong(username)));
    }
}
