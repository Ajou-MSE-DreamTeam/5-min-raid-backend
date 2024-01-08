package com.momong.five_min_raid.global.auth.dto.response;

import com.momong.five_min_raid.domain.member.dto.MemberDto;
import com.momong.five_min_raid.domain.member.dto.response.MemberResponse;
import com.momong.five_min_raid.global.auth.dto.AccessAndRefreshTokensInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponse {

    @Schema(description = "로그인 유저 정보")
    private MemberResponse loggedInMember;

    @Schema(description = "Access token, refresh token 정보")
    private AccessAndRefreshTokensResponse tokens;

    public static LoginResponse from(MemberDto memberDto, AccessAndRefreshTokensInfoDto accessAndRefreshTokensInfoDto) {
        return new LoginResponse(MemberResponse.from(memberDto), AccessAndRefreshTokensResponse.from(accessAndRefreshTokensInfoDto));
    }
}
