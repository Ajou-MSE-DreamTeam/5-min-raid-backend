package com.momong.backend.domain.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {

    USER("ROLE_USER", "사용자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    ;

    private final String roleName;
    private final String description;
}
