package com.momong.five_min_raid.domain.member_game_record.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record GuardianPerkRequest(
        @Schema(description = "이름", example = "DEFENSE_RECOVERY")
        String name,

        @Schema(description = "레벨", example = "2")
        Integer level
) {
}
