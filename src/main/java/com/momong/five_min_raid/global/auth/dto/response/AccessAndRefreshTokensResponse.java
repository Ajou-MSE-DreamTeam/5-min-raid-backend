package com.momong.five_min_raid.global.auth.dto.response;

import com.momong.five_min_raid.global.auth.dto.AccessAndRefreshTokensInfoDto;
import com.momong.five_min_raid.global.auth.dto.JwtTokenInfoDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class AccessAndRefreshTokensResponse {

    @Schema(description = "Access token")
    private TokenResponse accessToken;

    @Schema(description = "Refresh token")
    private TokenResponse refreshToken;

    public static AccessAndRefreshTokensResponse from(AccessAndRefreshTokensInfoDto accessAndRefreshTokensInfoDto) {
        JwtTokenInfoDto accessToken = accessAndRefreshTokensInfoDto.accessToken();
        JwtTokenInfoDto refreshToken = accessAndRefreshTokensInfoDto.refreshToken();
        return new AccessAndRefreshTokensResponse(
                new TokenResponse(accessToken.token(), accessToken.expiresAt()),
                new TokenResponse(refreshToken.token(), refreshToken.expiresAt())
        );
    }
}