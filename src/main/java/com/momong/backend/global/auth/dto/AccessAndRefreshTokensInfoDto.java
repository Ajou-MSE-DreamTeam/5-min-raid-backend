package com.momong.backend.global.auth.dto;

public record AccessAndRefreshTokensInfoDto(
        JwtTokenInfoDto accessToken,
        JwtTokenInfoDto refreshToken
) {
}
