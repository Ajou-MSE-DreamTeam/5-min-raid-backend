package com.momong.five_min_raid.domain.member.dto.response;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MemberResponse {

    @Schema(description = "PK of member", example = "1")
    private Long id;

    @Schema(description = "Nickname. 신규 회원의 경우에는 ", example = "woogie")
    private String nickname;

    public static MemberResponse from(MemberDto memberDto) {
        return new MemberResponse(memberDto.getId(), memberDto.getNickname());
    }
}
