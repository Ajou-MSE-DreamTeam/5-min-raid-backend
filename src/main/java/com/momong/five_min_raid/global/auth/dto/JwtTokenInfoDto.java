package com.momong.five_min_raid.global.auth.dto;

import java.time.LocalDateTime;

public record JwtTokenInfoDto (
        String token,
        LocalDateTime expiresAt
) {
}
