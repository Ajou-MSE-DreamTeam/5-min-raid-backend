package com.momong.five_min_raid.global.auth;

import com.momong.five_min_raid.domain.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class CustomUserDetailsService {

    @Bean
    public UserDetailsService userDetailsService(MemberService memberService) {
        return username -> new UserPrincipal(memberService.getDtoById(Long.parseLong(username)));
    }
}
