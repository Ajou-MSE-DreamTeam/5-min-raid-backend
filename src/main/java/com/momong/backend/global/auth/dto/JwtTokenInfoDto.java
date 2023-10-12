package com.momong.backend.global.auth.dto;

import java.time.LocalDateTime;

public record JwtTokenInfoDto (
        String token,
        LocalDateTime expiresAt
) {
}
