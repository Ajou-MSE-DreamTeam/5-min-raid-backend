package com.momong.five_min_raid.global.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenValidationResponse {

    @Schema(description = "유효성 여부. 유효한 토큰이라면 true", example = "true")
    private boolean validity;
}
