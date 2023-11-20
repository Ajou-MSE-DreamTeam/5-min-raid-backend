package com.momong.backend.domain.game_record.dto.response;

import com.momong.backend.domain.game_record.dto.GameRecordDto;
import com.momong.backend.global.common.constant.TeamType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GameRecordResponse {

    @Schema(description = "PK of game record", example = "2")
    @NotNull
    private Long id;

    @Schema(description = "승리한 팀")
    @NotNull
    private TeamType winnerTeam;

    @Schema(description = "게임 시작 시간")
    @NotNull
    private LocalDateTime startedAt;

    @Schema(description = "게임 종료 시간")
    @NotNull
    private LocalDateTime endedAt;

    public static GameRecordResponse from(@NotNull GameRecordDto gameRecordDto) {
        return new GameRecordResponse(
                gameRecordDto.getId(),
                gameRecordDto.getWinnerTeam(),
                gameRecordDto.getStartedAt(),
                gameRecordDto.getEndedAt()
        );
    }
}
