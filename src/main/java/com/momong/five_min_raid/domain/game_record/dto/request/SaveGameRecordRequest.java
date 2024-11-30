package com.momong.five_min_raid.domain.game_record.dto.request;

import com.momong.five_min_raid.domain.game_record.entity.GameRecord;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveGuardianGameRecordRequest;
import com.momong.five_min_raid.domain.member_game_record.dto.request.SaveMonsterGameRecordRequest;
import com.momong.five_min_raid.global.common.constant.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    public GameRecord toEntity() {
        return GameRecord.create(
                this.winnerTeam,
                this.startedAt,
                this.endedAt
        );
    }
}
