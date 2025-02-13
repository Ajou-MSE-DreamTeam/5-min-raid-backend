package com.momong.five_min_raid.domain.member.dto.response;

import com.momong.five_min_raid.domain.member.constant.NicknameUnavailableReason;
import io.swagger.v3.oas.annotations.media.Schema;

public record CheckNicknameAvailabilityResponse(
        @Schema(description = "닉네임", example = "치와외")
        String nickname,

        @Schema(description = "이용 가능 여부")
        Boolean isAvailable,

        @Schema(description = "이용 불가능한 경우, 불가능한 이유")
        NicknameUnavailableReason unavailableReason
) {
}
