package com.momong.five_min_raid.domain.game_record.dto.request;

import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveGuardianGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveMonsterGameRecordRequest;
import com.momong.five_min_raid.global.common.constant.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SaveGameRecordRequest {

    @Schema(description = "승리 팀")
    @NotNull
    private TeamType winnerTeam;

    @Schema(description = "몬스터의 게임 기록 정보")
    @NotNull
    private SaveMonsterGameRecordRequest monster;

    @Schema(description = "가디언들의 게임 기록 정보")
    @NotNull
    private List<SaveGuardianGameRecordRequest> guardians;

    @Schema(description = "게임 시작 시간")
    @NotNull
    private LocalDateTime startedAt;

    @Schema(description = "게임 종료 시간")
    @NotNull
    private LocalDateTime endedAt;

    @Schema(description = "(가디언) 획득한 경험치량", example = "300")
    private Integer exp;

    @Schema(description = "1 페이즈에 소요된 시간(초)", example = "60")
    private Integer phase1Time;

    @Schema(description = "2 페이즈에 소요된 시간(초)", example = "100")
    private Integer phase2Time;

    @Schema(description = "3 페이즈에 소요된 시간(초)", example = "120")
    private Integer phase3Time;

    @Schema(description = "사용된 맵 기믹 리스트")
    private List<String> mapGimmicks;
}
