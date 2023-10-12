package com.momong.backend.domain.member.dto;

import com.momong.backend.domain.member.constant.RoleType;
import com.momong.backend.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberDto {

    private Long id;
    private String socialUid;
    private Set<RoleType> roleTypes;
    private String nickname;

    public static MemberDto from(Member member) {
        return new MemberDto(
                member.getId(),
                member.getSocialUid(),
                member.getRoleTypes(),
                member.getNickname()
        );
    }
}
