package com.momong.five_min_raid.global.auth.dto;

public record AccessAndRefreshTokensInfoDto(
        JwtTokenInfoDto accessToken,
        JwtTokenInfoDto refreshToken
) {
}
