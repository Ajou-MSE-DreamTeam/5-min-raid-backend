package com.momong.backend.domain.game_record.dto;

import com.momong.backend.domain.game_record.entity.GameRecord;
import com.momong.backend.global.common.constant.TeamType;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class GameRecordDto {

    @NotNull
    private Long id;

    @NotNull
    private TeamType winnerTeam;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime endedAt;

    public static GameRecordDto from(@NotNull GameRecord gameRecord) {
        return new GameRecordDto(
                gameRecord.getId(),
                gameRecord.getWinnerTeam(),
                gameRecord.getStartedAt(),
                gameRecord.getEndedAt()
        );
    }
}
