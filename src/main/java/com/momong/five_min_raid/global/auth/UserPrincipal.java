package com.momong.five_min_raid.global.auth;

import com.momong.five_min_raid.domain.member.constant.RoleType;
import com.momong.five_min_raid.domain.member.dto.MemberDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private MemberDto memberDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return memberDto.getRoleTypes().stream()
                .map(RoleType::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Long getMemberId() {
        return memberDto.getId();
    }

    @Override
    public String getUsername() {
        return String.valueOf(getMemberId());
    }

    @Override
    public String getPassword() {
        return memberDto.getSocialUid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
