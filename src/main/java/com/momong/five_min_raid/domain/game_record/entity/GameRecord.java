package com.momong.five_min_raid.domain.game_record.entity;

import com.momong.five_min_raid.global.common.constant.TeamType;
import com.momong.five_min_raid.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class GameRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_record_id", nullable = false)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TeamType winnerTeam;

    @NotNull
    private LocalDateTime startedAt;

    @NotNull
    private LocalDateTime endedAt;

    private Integer phase1Time;

    private Integer phase2Time;

    private Integer phase3Time;

    public static GameRecord create(
            @NotNull TeamType winnerTeam,
            @NotNull LocalDateTime startedAt,
            @NotNull LocalDateTime endedAt,
            Integer phase1Time,
            Integer phase2Time,
            Integer phase3Time
    ) {
        return new GameRecord(null, winnerTeam, startedAt, endedAt, phase1Time, phase2Time, phase3Time);
    }
}
